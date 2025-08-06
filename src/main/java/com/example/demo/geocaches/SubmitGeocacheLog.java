package com.example.demo.geocaches;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.util.function.Consumer;

@Tag("div")
class SubmitGeocacheLog extends Div {

  private final Binder<GeocacheLogEntity> binder = new Binder<>(GeocacheLogEntity.class);
  private final Consumer<GeocacheLogEntity> saveLogFunction;

  SubmitGeocacheLog(Consumer<GeocacheLogEntity> saveLogFunction) {
    this.saveLogFunction = saveLogFunction;
    addComponents();
  }

  private void addComponents() {
    var submitLogHeader = new H2("Dodaj wpis do logu");

    ComboBox<String> logType = new ComboBox<>("Typ:");
    logType.setItems("Znaleziona", "Nieznaleziona", "Komentarz");
    logType.setValue("Znaleziona");
    logType.setRequired(true);

    var date = new DatePicker("Data:");
    date.setValue(LocalDate.now());
    date.setRequired(true);

    var logText = new TextArea();
    logText.setWidthFull();
    logText.setRequired(true);

    var submitLogButton = new Button("Dodaj wpis");
    submitLogButton.addClickListener(e -> submitLog(logType.getValue(), date.getValue(), logText.getValue()));

    add(submitLogHeader, logType, date, logText, submitLogButton);
    addValidation(logType, date, logText);
  }

  private void submitLog(String logType, LocalDate date, String logText) {
    var log = new GeocacheLogEntity(logType, date, logText, null);
    if (!binder.writeBeanIfValid(log))
      return;
    saveLogFunction.accept(log);
    Notification.show("Dodano wpis", 3000, Notification.Position.TOP_CENTER)
      .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private void addValidation(ComboBox<String> logType, DatePicker date, TextArea logText) {
    binder.forField(logType)
      .asRequired()
      .bind(GeocacheLogEntity::getType, GeocacheLogEntity::setType);
    binder.forField(date)
      .asRequired()
      .withValidator(new DateRangeValidator("Data nie może być z przyszłości", LocalDate.MIN, LocalDate.now()))
      .bind(GeocacheLogEntity::getDate, GeocacheLogEntity::setDate);
    binder.forField(logText)
      .asRequired()
      .bind(GeocacheLogEntity::getText, GeocacheLogEntity::setText);
    var beanValidationErrors = new Div();
    beanValidationErrors.addClassName(LumoUtility.TextColor.ERROR);
    binder.setStatusLabel(beanValidationErrors);
  }

}
