package com.example.demo.geocaches;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("/create_listing")
@PermitAll
public class CreateListing extends VerticalLayout {

  @Autowired
  private GeocacheRepository geocacheRepository;

  private final Binder<GeocacheEntity> binder = new Binder<>(GeocacheEntity.class);

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    addClassName("centered-content");

    var name = addTextField("Nazwa skrzynki:", VaadinIcon.FILE_FONT.create());
    var owner = addTextField("Właściciel:", VaadinIcon.USER.create()); // TODO remove after adding userness
    var type = addRadioButton("Typ:", VaadinIcon.PUZZLE_PIECE.create(), List.of("tradycyjna", "multicache", "zagadkowa", "nietypowa"));

    // TODO add some better input method for coords
    var coordLat = addTextField("Szerokość geograficzna (N/S):", VaadinIcon.LOCATION_ARROW_CIRCLE_O.create());
    coordLat.setHelperText("Format: 52.09232 for N or with '-' for S");
    coordLat.setMinWidth(300, Unit.PIXELS);
    var coordLon = addTextField("Długość geograficzna (E/W):", VaadinIcon.LOCATION_ARROW_CIRCLE_O.create());
    coordLon.setHelperText("Format: 21.32782 for E or with '-' for W");
    coordLon.setMinWidth(300, Unit.PIXELS);

    var state = addRadioButton("Status:", VaadinIcon.FLAG.create(), List.of("aktywna", "nieaktywna"));
    var size = addRadioButton("Wielkość:", VaadinIcon.CUBE.create(), List.of("mikro", "mała", "średnia", "duża"));
    var difficulty = addRadioButton("Trudność:", VaadinIcon.ABACUS.create(), List.of("1/5", "2/5", "3/5", "4/5", "5/5"));
    var attributes = addAttributes();
    var description = addTextArea("Opis:", VaadinIcon.EDIT.create());
    var spoiler = addTextArea("Spoiler:", VaadinIcon.LIGHTBULB.create());

    addValidation(name, owner, type, coordLat, coordLon, state, size, difficulty, attributes, description, spoiler);
    var saveButton = new Button("Zapisz");
    saveButton.addClickListener(clickEvent -> {
      saveGeocacheAndGoHome(name, owner, type, coordLat, coordLon, state, size, difficulty, attributes, description, spoiler);
    });
    add(saveButton);
  }

  private void addValidation(TextField name, TextField owner, RadioButtonGroup<String> type, TextField coordLat, TextField coordLon,
                             RadioButtonGroup<String> state, RadioButtonGroup<String> size, RadioButtonGroup<String> difficulty,
                             CheckboxGroup<String> attributes, TextArea description, TextArea spoiler) {
    binder.forField(name)
      .asRequired()
      .bind(GeocacheEntity::getName, GeocacheEntity::setName);
    binder.forField(owner)
      .asRequired()
      .bind(GeocacheEntity::getOwner, GeocacheEntity::setOwner);
    binder.forField(type)
      .asRequired()
      .bind(GeocacheEntity::getType, GeocacheEntity::setType);
    binder.forField(coordLat)
      .asRequired()
      .withValidator(new RegexpValidator("Invalid coord format", "-?\\d{1,2}\\.\\d+"))
      .bind(entity -> entity.getCoordLat().toString(), (entity, val) -> entity.setCoordLat(Double.parseDouble(val)));
    binder.forField(coordLon)
      .asRequired()
      .withValidator(new RegexpValidator("Invalid coord format", "-?\\d{1,2}\\.\\d+"))
      .bind(entity -> entity.getCoordLon().toString(), (entity, val) -> entity.setCoordLon(Double.parseDouble(val)));
    binder.forField(state)
      .asRequired()
      .bind(GeocacheEntity::getState, GeocacheEntity::setState);
    binder.forField(size)
      .asRequired()
      .bind(GeocacheEntity::getSize, GeocacheEntity::setSize);
    binder.forField(difficulty)
      .asRequired()
      .bind(GeocacheEntity::getDifficulty, GeocacheEntity::setDifficulty);
    binder.forField(attributes)
      .bind(GeocacheEntity::getAttributes, GeocacheEntity::setAttributes);
    binder.forField(description)
      .asRequired()
      .bind(GeocacheEntity::getDescription, GeocacheEntity::setDescription);
    binder.forField(spoiler)
      .asRequired()
      .bind(GeocacheEntity::getSpoiler, GeocacheEntity::setSpoiler);

    var beanValidationErrors = new Div();
    beanValidationErrors.addClassName(LumoUtility.TextColor.ERROR);
    binder.setStatusLabel(beanValidationErrors);
  }

  private TextField addTextField(String label, Icon icon) {
    var textField = new TextField();
    textField.setLabel(label);
    textField.setRequired(true);
    textField.setPrefixComponent(icon);
    add(textField);
    return textField;
  }

  private RadioButtonGroup<String> addRadioButton(String labelText, Icon icon, List<String> options) {
    RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
    //radioGroup.getElement().appendChild(label(labelText, icon)); // TODO make it work
    radioGroup.setLabel(labelText); //
    radioGroup.setItems(options);
    radioGroup.setValue(options.iterator().next());
    radioGroup.setRequired(true);
    add(radioGroup);
    return radioGroup;
  }

  private static Element label(String label, Icon icon) {
    var labelSpan = new Span(label);
    var labelLayout = new HorizontalLayout(icon, labelSpan);
    labelLayout.setAlignItems(Alignment.CENTER);
    labelLayout.setPadding(false);
    labelLayout.setSpacing(true);
    labelLayout.getElement().setAttribute("slot", "label");
    labelLayout.setId("label-" + java.util.UUID.randomUUID());
    return labelLayout.getElement();
  }

  private CheckboxGroup<String> addAttributes() {
    CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    //checkboxGroup.getElement().appendChild(label("Atrybuty:", VaadinIcon.LINES_LIST.create()));
    checkboxGroup.setLabel("Atrybuty:"); //
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

  private void saveGeocacheAndGoHome(TextField name, TextField owner, RadioButtonGroup<String> type, TextField coordLat, TextField coordLon,
                                     RadioButtonGroup<String> state, RadioButtonGroup<String> size, RadioButtonGroup<String> difficulty,
                                     CheckboxGroup<String> attributes, TextArea description, TextArea spoiler
  ) {
    var geocache = new GeocacheEntity();
    geocache.name = name.getValue();
    geocache.owner = owner.getValue();
    geocache.type = type.getValue();
    try {
      geocache.coordLat = Double.parseDouble(coordLat.getValue());
      geocache.coordLon = Double.parseDouble(coordLon.getValue());
    } catch (NumberFormatException e) {/*ignored*/}
    geocache.state = state.getValue();
    geocache.size = size.getValue();
    geocache.difficulty = difficulty.getValue();
    geocache.attributes = attributes.getSelectedItems();
    geocache.description = description.getValue();
    geocache.spoiler = spoiler.getValue();

    if (!binder.writeBeanIfValid(geocache))
      return;

    double lat = geocache.getCoordLat();
    double lon = geocache.getCoordLon();
    // TODO use format: N52° 05.539' E21° 19.669'
    geocache.coordsVisible = (lat > 0 ? "N" : "S") + Math.abs(lat) + "° " + (lon > 0 ? "E" : "W") + Math.abs(lon) + "°";
    geocacheRepository.save(geocache);
    UI.getCurrent().navigate("");
  }

}