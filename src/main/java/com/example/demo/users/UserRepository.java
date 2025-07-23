package com.example.demo.users;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUserName(String username);
}