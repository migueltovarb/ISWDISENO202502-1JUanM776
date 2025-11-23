package com.garageid.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
@ConditionalOnBean(MongoTemplate.class)
public class MongoIndexesConfig {
  private final MongoTemplate template;

  public MongoIndexesConfig(MongoTemplate template) { this.template = template; }

  @Bean
  public CommandLineRunner createIndexes() {
    return args -> {
      try {
        var users = template.indexOps("users");
        users.ensureIndex(new Index().on("email", Sort.Direction.ASC).unique());

        var vehicles = template.indexOps("vehicles");
        vehicles.ensureIndex(new Index().on("userId", Sort.Direction.ASC));
        vehicles.ensureIndex(new Index().on("plate", Sort.Direction.ASC).unique());

        var reservations = template.indexOps("reservations");
        reservations.ensureIndex(new Index().on("userId", Sort.Direction.ASC));
        reservations.ensureIndex(new Index().on("from", Sort.Direction.ASC));
        reservations.ensureIndex(new Index().on("to", Sort.Direction.ASC));

        var payments = template.indexOps("payments");
        payments.ensureIndex(new Index().on("userId", Sort.Direction.ASC));
        payments.ensureIndex(new Index().on("createdAt", Sort.Direction.ASC));

        var parking = template.indexOps("parking");
        parking.ensureIndex(new Index().on("name", Sort.Direction.ASC));
      } catch (Exception ignored) {}
    };
  }
}
