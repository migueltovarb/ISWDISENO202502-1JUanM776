package com.garageid.config;

import com.garageid.repository.*;
import com.garageid.repository.inmemory.*;
import com.garageid.repository.mongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class RepoConfig {
  private final MongoTemplate mongo;

  public RepoConfig(@Autowired(required = false) MongoTemplate mongo) { this.mongo = mongo; }

  @Bean public UserRepository userRepository() { return mongo != null ? new MongoUserRepository(mongo) : new InMemoryUserRepository(); }
  @Bean public VehicleRepository vehicleRepository() { return mongo != null ? new MongoVehicleRepository(mongo) : new InMemoryVehicleRepository(); }
  @Bean public ParkingLotRepository parkingLotRepository() { return mongo != null ? new MongoParkingLotRepository(mongo) : new InMemoryParkingLotRepository(); }
  @Bean public ReservationRepository reservationRepository() { return mongo != null ? new MongoReservationRepository(mongo) : new InMemoryReservationRepository(); }
  @Bean public PaymentRepository paymentRepository() { return mongo != null ? new MongoPaymentRepository(mongo) : new InMemoryPaymentRepository(); }
}
