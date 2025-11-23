package com.garageid.repository.mongo;

import com.garageid.model.Vehicle;
import com.garageid.repository.VehicleRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoVehicleRepository implements VehicleRepository {
  private final MongoCollection<Document> col;

  public MongoVehicleRepository(MongoTemplate template) {
    this.col = template.getDb().getCollection("vehicles");
  }

  @Override
  public Vehicle save(Vehicle v) {
    Document d = new Document("_id", v.getId())
        .append("userId", v.getUserId())
        .append("plate", v.getPlate())
        .append("brand", v.getBrand())
        .append("model", v.getModel())
        .append("type", v.getType())
        .append("color", v.getColor());
    col.replaceOne(Filters.eq("_id", v.getId()), d, new ReplaceOptions().upsert(true));
    return v;
  }

  @Override
  public Optional<Vehicle> findById(String id) {
    Document d = col.find(Filters.eq("_id", id)).first();
    return Optional.ofNullable(map(d));
  }

  @Override
  public List<Vehicle> findByUserId(String userId) {
    List<Vehicle> out = new ArrayList<>();
    for (Document d : col.find(Filters.eq("userId", userId))) out.add(map(d));
    return out;
  }

  @Override
  public void deleteById(String id) {
    col.deleteOne(Filters.eq("_id", id));
  }

  private Vehicle map(Document d) {
    if (d == null) return null;
    return new Vehicle(
        d.getString("_id"),
        d.getString("userId"),
        d.getString("plate"),
        d.getString("brand"),
        d.getString("model"),
        d.getString("type"),
        d.getString("color")
    );
  }
}
