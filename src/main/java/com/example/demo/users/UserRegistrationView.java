package com.example.demo.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@PageTitle("Sign-up - GeocachingWebApp")
@AnonymousAllowed
public class UserRegistrationView extends VerticalLayout {

  @Autowired
  private UserRegistrationService userRegistrationService;

  private final TextField username = new TextField("User name");
  private final PasswordField password = new PasswordField("Password");

  private final BeanValidationBinder<RegistrationForm> binder = new BeanValidationBinder<>(RegistrationForm.class);

  public UserRegistrationView() {
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    setupBinder();

    Button registerButton = new Button("Register");
    registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    registerButton.addClickListener(e -> onRegisterClick());

    RouterLink registrationLink = new RouterLink("Sign-in", UserSignInView.class);
    add(new H1("Sign-up - GeocachingWebApp"), username, password, registerButton, registrationLink);
  }

  private void setupBinder() {
    binder.forField(username).asRequired().bind("username");
    binder.forField(password).asRequired().bind("password");
    binder.setBean(new RegistrationForm());
  }

  private void onRegisterClick() {
    try {
      RegistrationForm registrationForm = new RegistrationForm();
      binder.writeBean(registrationForm);

      userRegistrationService.register(registrationForm.getUsername(), registrationForm.getPassword());

      Notification.show("Success", 3000, Notification.Position.TOP_CENTER)
        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      UI.getCurrent().navigate("login");

    } catch (ValidationException e) {
      Notification.show("Invalid login or password", 3000, Notification.Position.TOP_CENTER)
        .addThemeVariants(NotificationVariant.LUMO_ERROR);
    } catch (UserRegistrationService.UserAlreadyExistsException e) {
      Notification.show(e.getMessage(), 3000, Notification.Position.TOP_CENTER)
        .addThemeVariants(NotificationVariant.LUMO_ERROR);
      username.setInvalid(true);
      username.setErrorMessage(e.getMessage());
    }
  }

  public static class RegistrationForm {

    private String username = "";
    private String password = "";

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

  }

}

