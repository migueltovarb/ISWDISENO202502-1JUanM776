package com.garageid.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
  private final MongoTemplate mongoTemplate;
  private final String mongoUri;

  public HealthController(MongoTemplate mongoTemplate,
                          @Value("${spring.data.mongodb.uri:}") String mongoUri) {
    this.mongoTemplate = mongoTemplate;
    this.mongoUri = mongoUri;
  }

  @GetMapping("/mongo")
  public ResponseEntity<Map<String,Object>> mongo() {
    Map<String,Object> body = new HashMap<>();
    if (mongoUri == null || mongoUri.isBlank()) {
      body.put("status","disabled");
      return ResponseEntity.ok(body);
    }
    try {
      String db = mongoTemplate.getDb().getName();
      body.put("status","ok");
      body.put("database", db);
      return ResponseEntity.ok(body);
    } catch (Exception e) {
      body.put("status","error");
      body.put("message", e.getMessage());
      return ResponseEntity.status(500).body(body);
    }
  }
}
