package com.garageid.repository.mongo;

import com.garageid.model.ParkingLot;
import com.garageid.repository.ParkingLotRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoParkingLotRepository implements ParkingLotRepository {
  private final MongoCollection<Document> col;

  public MongoParkingLotRepository(MongoTemplate template) {
    this.col = template.getDb().getCollection("parking");
  }

  @Override
  public ParkingLot save(ParkingLot p) {
    Document d = new Document("_id", p.getId())
        .append("name", p.getName())
        .append("capacity", p.getCapacity())
        .append("location", p.getLocation());
    col.replaceOne(Filters.eq("_id", p.getId()), d, new ReplaceOptions().upsert(true));
    return p;
  }

  @Override
  public Optional<ParkingLot> findById(String id) {
    Document d = col.find(Filters.eq("_id", id)).first();
    return Optional.ofNullable(map(d));
  }

  @Override
  public List<ParkingLot> findAll() {
    List<ParkingLot> out = new ArrayList<>();
    for (Document d : col.find()) out.add(map(d));
    return out;
  }

  @Override
  public void deleteById(String id) { col.deleteOne(Filters.eq("_id", id)); }

  private ParkingLot map(Document d) {
    if (d == null) return null;
    return new ParkingLot(
        d.getString("_id"),
        d.getString("name"),
        d.getInteger("capacity", 0),
        d.getString("location")
    );
  }
}
