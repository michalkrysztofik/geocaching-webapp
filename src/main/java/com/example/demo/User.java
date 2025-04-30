package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

  @Id
  private String userId;
  public String name;

  public User(String name) {
    super();
    this.name = name;
  }

}