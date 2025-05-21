package com.example.demo.geocaches;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeocacheRepository extends MongoRepository<GeocacheEntity,String> {
  GeocacheEntity getByCode(String code);
}