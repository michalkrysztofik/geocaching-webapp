package com.example.demo.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImp(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username)
      .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));

    List<GrantedAuthority> authorities = user.roles.stream()
      .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
      .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(
      user.userName,
      user.hashedPassword,
      authorities
    );
  }

}
