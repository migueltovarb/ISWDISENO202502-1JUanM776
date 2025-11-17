package com.garageid.access;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessEventRepository extends MongoRepository<AccessEvent, String> {
}