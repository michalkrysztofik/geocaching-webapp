package com.example.demo.geocaches;

import com.example.demo.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/listing")
public class Listing extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle {

  @Autowired
  private GeocacheRepository geocacheRepository;
  private GeocacheEntity geocache;

  public Listing() {
  }

  @Override
  public void setParameter(BeforeEvent event, String geocacheCode) {
    geocache = geocacheRepository.getByCode(geocacheCode);
  }

  @Override
  public String getPageTitle() {
    return geocache.name;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    addClassName("centered-content");
    var title = new H1(geocache.name + " - " + geocache.code);

    var basicInfo = new Div();
    var listingOwner = new Paragraph("Właściciel: " + geocache.owner);
    var type = new Paragraph("Typ: " + geocache.type);
    var coords = new Paragraph("Współrzędne: " + geocache.coordsVisible);
    var state = new Paragraph("Status: " + geocache.state);
    var size = new Paragraph("Wielkość: " + geocache.size);
    var difficulty = new Paragraph("Trudność: " + geocache.difficulty);
    basicInfo.add(listingOwner, type, coords, state, size, difficulty);

    var attributes = new Details("Atrybuty:", new UnorderedList(
      geocache.attributes.stream().map(ListItem::new).toArray(ListItem[]::new)
    ));
    attributes.setOpened(true);

    var description = new Paragraph(geocache.description);

    var spoiler = new Details("Spoiler:", new Paragraph(geocache.spoiler));
    spoiler.setOpened(false);

    var photos = new Div("Zdjęcia: TODO");
    var waypoints = new Div("Dodatkowe interesujące miejsa: TODO");

    add(title, basicInfo, attributes, description, spoiler, photos, waypoints, new RouterLink("GO BACK", MainView.class));
  }

}
