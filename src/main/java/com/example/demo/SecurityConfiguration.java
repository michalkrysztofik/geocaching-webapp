package com.example.demo;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Delegating the responsibility of general configurations
    // of http security to the super class. It's configuring
    // the followings: Vaadin's CSRF protection by ignoring
    // framework's internal requests, default request cache,
    // ignoring public views annotated with @AnonymousAllowed,
    // restricting access to other views/endpoints, and enabling
    // NavigationAccessControl authorization.
    // You can add any possible extra configurations of your own
    // here (the following is just an example):

    // http.rememberMe().alwaysRemember(false);

    // Configure your static resources with public access before calling
    // super.configure(HttpSecurity) as it adds final anyRequest matcher
    http.authorizeHttpRequests(auth -> auth
      .requestMatchers("/public/**")
      .permitAll());

    super.configure(http);

    setLoginView(http, LoginView.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }

  @Bean
  public UserDetailsManager userDetailsService() {
    UserDetails user = org.springframework.security.core.userdetails.User.withUsername("user")
      .password("{noop}1234")
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }

}