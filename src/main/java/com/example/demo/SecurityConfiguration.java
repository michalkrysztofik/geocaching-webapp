package com.example.demo;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {
  // https://vaadin.com/blog/oauth-2-and-google-sign-in-for-a-vaadin-application

  private static final String LOGIN_URL = "/login";

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.oauth2Login().loginPage(LOGIN_URL).permitAll();
    //http.oauth2Login(Customizer.withDefaults()).loginPage(LOGIN_URL).permitAll();
  }

}