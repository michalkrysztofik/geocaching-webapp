package com.example.demo.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "users")
public class User {

  @Id
  private String userId;
  public String userName;
  public String hashedPassword;
  public Set<Role> roles;

  public User(String userName, String hashedPassword) {
    super();
    this.userName = userName;
    this.hashedPassword = hashedPassword;
    this.roles = Set.of(Role.USER);
  }

  public enum Role {
    USER, ADMIN;
  }

}