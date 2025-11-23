package com.garageid.repository.mongo;

import com.garageid.model.Reservation;
import com.garageid.repository.ReservationRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoReservationRepository implements ReservationRepository {
  private final MongoCollection<Document> col;
  private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public MongoReservationRepository(MongoTemplate template) {
    this.col = template.getDb().getCollection("reservations");
  }

  @Override
  public Reservation save(Reservation r) {
    Document d = new Document("_id", r.getId())
        .append("userId", r.getUserId())
        .append("vehicleId", r.getVehicleId())
        .append("parkingLotId", r.getParkingLotId())
        .append("from", F.format(r.getFrom()))
        .append("to", F.format(r.getTo()));
    col.replaceOne(Filters.eq("_id", r.getId()), d, new ReplaceOptions().upsert(true));
    return r;
  }

  @Override
  public Optional<Reservation> findById(String id) {
    Document d = col.find(Filters.eq("_id", id)).first();
    return Optional.ofNullable(map(d));
  }

  @Override
  public List<Reservation> findByUserId(String userId) {
    List<Reservation> out = new ArrayList<>();
    for (Document d : col.find(Filters.eq("userId", userId))) out.add(map(d));
    return out;
  }

  @Override
  public List<Reservation> findBetween(LocalDateTime from, LocalDateTime to) {
    List<Reservation> out = new ArrayList<>();
    for (Document d : col.find(Filters.and(
        Filters.gte("from", F.format(from)),
        Filters.lte("to", F.format(to))))) out.add(map(d));
    return out;
  }

  @Override
  public void deleteById(String id) { col.deleteOne(Filters.eq("_id", id)); }

  private Reservation map(Document d) {
    if (d == null) return null;
    return new Reservation(
        d.getString("_id"),
        d.getString("userId"),
        d.getString("vehicleId"),
        d.getString("parkingLotId"),
        LocalDateTime.parse(d.getString("from"), F),
        LocalDateTime.parse(d.getString("to"), F)
    );
  }
}
