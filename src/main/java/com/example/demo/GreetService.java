package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GreetService {

  @Autowired
  private UserRepository userRepo;

  public String greet(String name) {
    if (name == null || name.isEmpty()) {
      return userRepo.findAll().stream()
        .findFirst()
        .map(u -> "Hello " + u.name)
        .orElse("Hello anonymous user");
    } else {
      userRepo.deleteAll();
      userRepo.save(new User(name));
      return "Hello " + name;
    }
  }
}
