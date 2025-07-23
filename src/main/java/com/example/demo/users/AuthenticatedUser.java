package com.example.demo.users;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

  private final UserRepository userRepository;
  private final AuthenticationContext authenticationContext;

  public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
    this.authenticationContext = authenticationContext;
    this.userRepository = userRepository;
  }

  public User get() {
    return authenticationContext.getAuthenticatedUser(UserDetails.class)
      .flatMap(userDetails -> userRepository.findByUserName(userDetails.getUsername()))
      .orElseThrow();
  }

}