package com.garageid.repository.mongo;

import com.garageid.model.Payment;
import com.garageid.repository.PaymentRepository;
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

public class MongoPaymentRepository implements PaymentRepository {
  private final MongoCollection<Document> col;
  private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public MongoPaymentRepository(MongoTemplate template) {
    this.col = template.getDb().getCollection("payments");
  }

  @Override
  public Payment save(Payment p) {
    Document d = new Document("_id", p.getId())
        .append("userId", p.getUserId())
        .append("reservationId", p.getReservationId())
        .append("amount", p.getAmount())
        .append("status", p.getStatus())
        .append("createdAt", F.format(p.getCreatedAt()))
        .append("method", p.getMethod());
    col.replaceOne(Filters.eq("_id", p.getId()), d, new ReplaceOptions().upsert(true));
    return p;
  }

  @Override
  public Optional<Payment> findById(String id) {
    Document d = col.find(Filters.eq("_id", id)).first();
    return Optional.ofNullable(map(d));
  }

  @Override
  public List<Payment> findByUserId(String userId) {
    List<Payment> out = new ArrayList<>();
    for (Document d : col.find(Filters.eq("userId", userId))) out.add(map(d));
    return out;
  }

  private Payment map(Document d) {
    if (d == null) return null;
    return new Payment(
        d.getString("_id"),
        d.getString("userId"),
        d.getString("reservationId"),
        d.getDouble("amount"),
        d.getString("status"),
        d.getString("method"),
        LocalDateTime.parse(d.getString("createdAt"), F)
    );
  }
}
