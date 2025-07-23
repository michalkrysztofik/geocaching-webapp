package com.example.demo.users;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Sign-in - GeocachingWebApp")
@AnonymousAllowed
public class UserSignInView extends VerticalLayout implements BeforeEnterObserver {

  private final LoginForm login = new LoginForm();

  public UserSignInView() {
    addClassName("login-view");
    setSizeFull();

    setJustifyContentMode(JustifyContentMode.CENTER);
    setAlignItems(Alignment.CENTER);

    login.setAction("login");
    RouterLink registrationLink = new RouterLink("Sign-up", UserRegistrationView.class);

    add(new H1("Sign-in - GeocachingWebApp"), login, registrationLink);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error"))
      login.setError(true);
  }

}