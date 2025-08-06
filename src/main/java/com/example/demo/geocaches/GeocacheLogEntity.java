package com.example.demo.geocaches;

import java.time.LocalDate;

public class GeocacheLogEntity {

  public String type;
  public LocalDate date;
  public String text;
  public String userName;

  public GeocacheLogEntity(String type, LocalDate date, String text, String userName) {
    this.type = type;
    this.date = date;
    this.text = text;
    this.userName = userName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
