package com.example.demo.geocaches;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Route("/create_listing")
public class CreateListing extends VerticalLayout {

  @Autowired
  private GeocacheRepository geocacheRepository;

  public CreateListing() {
    addClassName("centered-content");

    var name = addTextField("Nazwa skrzynki", VaadinIcon.FILE_FONT.create());
    var owner = addTextField("Właściciel", VaadinIcon.USER.create());
    var type = addTextField("Typ", VaadinIcon.PUZZLE_PIECE.create());
    var coords = addTextField("Współrzędne", VaadinIcon.LOCATION_ARROW_CIRCLE_O.create());
    var state = addTextField("Status", VaadinIcon.FLAG.create());
    var size = addTextField("Wielkość", VaadinIcon.CUBE.create());
    var difficulty = addTextField("Trudność", VaadinIcon.ABACUS.create());
    var attributes = addTextField("Atrybuty", VaadinIcon.LINES_LIST.create());
    var description = addTextArea("Opis", VaadinIcon.EDIT.create());
    var spoiler = addTextArea("Spoiler", VaadinIcon.LIGHTBULB.create());

    var saveButton = new Button("Zapisz");
    saveButton.addClickListener(clickEvent -> {
      var geocache = new GeocacheEntity();
      geocache.name = name.getValue();
      geocache.owner = owner.getValue();
      geocache.type = type.getValue();
      geocache.coordsVisible = coords.getValue();
      geocache.coordLat = Double.parseDouble(geocache.coordsVisible.split(" ")[0]);
      geocache.coordLon = Double.parseDouble(geocache.coordsVisible.split(" ")[1]);
      geocache.state = state.getValue();
      geocache.size = size.getValue();
      geocache.difficulty = difficulty.getValue();
      geocache.attributes = Arrays.asList(attributes.getValue().split(","));
      geocache.description = description.getValue();
      geocache.spoiler = spoiler.getValue();
      geocacheRepository.save(geocache);
      UI.getCurrent().navigate("");
    });
    add(saveButton);
  }

  private TextField addTextField(String label, Icon icon) {
    var textField = new TextField();
    textField.setLabel(label);
    textField.setRequired(true);
    textField.setPrefixComponent(icon);
    add(textField);
    return textField;
  }

  private TextArea addTextArea(String label, Icon icon) {
    var textArea = new TextArea();
    textArea.setWidthFull();
    textArea.setLabel(label);
    add(textArea);
    return textArea;
  }

  /*@Override
  protected void onAttach(AttachEvent attachEvent) {
  }*/

}