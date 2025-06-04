package com.example.demo.geocaches;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("/create_listing")
public class CreateListing extends VerticalLayout {

  @Autowired
  private GeocacheRepository geocacheRepository;

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    addClassName("centered-content");

    var name = addTextField("Nazwa skrzynki:", VaadinIcon.FILE_FONT.create());
    var owner = addTextField("Właściciel:", VaadinIcon.USER.create()); // will be removed after adding userness
    // VaadinIcon.PUZZLE_PIECE.create()
    var type = addRadioButton("Typ:", List.of("tradycyjna", "multicache", "zagadkowa", "nietypowa"));

    // TODO something better
    var coordLat = addTextField("Szerokość geograficzna (N/S):", VaadinIcon.LOCATION_ARROW_CIRCLE_O.create());
    coordLat.setHelperText("Format: 52.09232 for N or with '-' for S");
    coordLat.setMinWidth(300, Unit.PIXELS);
    coordLat.setPattern("-?\\d{1,2}\\.\\d+");
    var coordLon = addTextField("Długość geograficzna (E/W):", VaadinIcon.LOCATION_ARROW_CIRCLE_O.create());
    coordLon.setHelperText("Format: 21.32782 for E or with '-' for W");
    coordLon.setMinWidth(300, Unit.PIXELS);
    coordLon.setPattern("-?\\d{1,3}\\.\\d+");

    // VaadinIcon.FLAG.create()
    var state = addRadioButton("Status:", List.of("aktywna", "nieaktywna"));
    // VaadinIcon.CUBE.create()
    var size = addRadioButton("Wielkość:", List.of("mikro", "mała", "średnia", "duża"));
    // VaadinIcon.ABACUS.create()
    var difficulty = addRadioButton("Trudność:", List.of("1/5", "2/5", "3/5", "4/5", "5/5"));
    var attributes = addAttributes();
    var description = addTextArea("Opis:", VaadinIcon.EDIT.create());
    var spoiler = addTextArea("Spoiler:", VaadinIcon.LIGHTBULB.create());

    var saveButton = new Button("Zapisz");
    saveButton.addClickListener(clickEvent -> {
      saveGeocache(name, owner, type, coordLat, coordLon, state, size, difficulty, attributes, description, spoiler);
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

  private RadioButtonGroup<String> addRadioButton(String label, List<String> options) {
    RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
    radioGroup.setLabel(label);
    radioGroup.setItems(options);
    radioGroup.setValue(options.iterator().next());
    add(radioGroup);
    return radioGroup;
  }

  private CheckboxGroup<String> addAttributes() {
    CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    // VaadinIcon.LINES_LIST.create()
    checkboxGroup.setLabel("Atrybuty:");
    checkboxGroup.setItems(
      "Szybka skrzynka",
      "Dostępna dla niepełnosprawnych",
      "Można zabrać dzieci",
      "Na łonie natury",
      "Potrzebny dodatkowy sprzęt",
      "Niebezpieczna",
      "Dostępna tylko pieszo",
      "Miejsce historyczne",
      "Potrzebna latarka",
      "Dostępna w określonych godzinach"
    );
    checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
    add(checkboxGroup);
    return checkboxGroup;
  }

  private TextArea addTextArea(String label, Icon icon) {
    var textArea = new TextArea();
    textArea.setWidthFull();
    textArea.setLabel(label);
    textArea.setPrefixComponent(icon);
    textArea.setRequired(true);
    add(textArea);
    return textArea;
  }

  private void saveGeocache(TextField name, TextField owner, RadioButtonGroup<String> type, TextField coordLat, TextField coordLon,
                            RadioButtonGroup<String> state, RadioButtonGroup<String> size, RadioButtonGroup<String> difficulty,
                            CheckboxGroup<String> attributes, TextArea description, TextArea spoiler
  ) {
    var geocache = new GeocacheEntity();
    geocache.name = name.getValue();
    geocache.owner = owner.getValue();
    geocache.type = type.getValue();

    double lat = Double.parseDouble(coordLat.getValue());
    double lon = Double.parseDouble(coordLon.getValue());
    // TODO use format: N52° 05.539' E21° 19.669'
    geocache.coordsVisible = (lat > 0 ? "N" : "S") + lat + "° " + (lon > 0 ? "E" : "W") + lon + "°";
    geocache.coordLat = lat;
    geocache.coordLon = lon;

    geocache.state = state.getValue();
    geocache.size = size.getValue();
    geocache.difficulty = difficulty.getValue();
    geocache.attributes = attributes.getSelectedItems();
    geocache.description = description.getValue();
    geocache.spoiler = spoiler.getValue();
    geocacheRepository.save(geocache);
  }

}