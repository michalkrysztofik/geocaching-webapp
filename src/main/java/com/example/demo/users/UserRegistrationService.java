package com.example.demo.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void register(String userName, String password) throws UserAlreadyExistsException {
    if (userRepository.findByUserName(userName).isPresent())
      throw new UserAlreadyExistsException(userName);

    User user = new User(userName, passwordEncoder.encode(password));
    userRepository.save(user);
  }

  public static class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String userName) {
      super("User '" + userName + "' already exists");
    }

  }

}
