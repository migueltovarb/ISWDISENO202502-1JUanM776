package com.garageid.config;

import com.garageid.repository.*;
import com.garageid.repository.inmemory.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepoConfig {
  @Bean public UserRepository userRepository() { return new InMemoryUserRepository(); }
  @Bean public VehicleRepository vehicleRepository() { return new InMemoryVehicleRepository(); }
  @Bean public ParkingLotRepository parkingLotRepository() { return new InMemoryParkingLotRepository(); }
  @Bean public ReservationRepository reservationRepository() { return new InMemoryReservationRepository(); }
  @Bean public PaymentRepository paymentRepository() { return new InMemoryPaymentRepository(); }
}