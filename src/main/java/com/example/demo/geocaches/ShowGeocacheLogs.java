package com.example.demo.geocaches;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;

import java.util.Collection;

@Tag("div")
class ShowGeocacheLogs extends Div {

  void load(GeocacheEntity geocache) {
    var showLogsHeader = new H2("Wpisy do logu");
    Collection<Component> logs = geocache.logs.stream()
      .map(ShowGeocacheLogs::formatLog)
      .toList();
    add(showLogsHeader);
    add(logs);
  }

  private static Component formatLog(GeocacheLogEntity log) {
    var logText = new Paragraph();
    logText.setText("> " + log.type + " | " + log.date + " | " + log.userName + "\n" + log.text);
    logText.getStyle().set("white-space", "pre-line");
    return logText;
  }

}
