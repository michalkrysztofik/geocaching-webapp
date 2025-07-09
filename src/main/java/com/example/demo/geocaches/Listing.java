package com.example.demo.geocaches;

import com.example.demo.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Route("/listing")
@PermitAll
public class Listing extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle {

  @Autowired
  private GeocacheRepository geocacheRepository;
  private GeocacheEntity geocache;

  private final H1 title = new H1();
  private final Map<String, Paragraph> basicInfoContent = new HashMap<>();
  private final Details attributes = new Details("Atrybuty:", new UnorderedList());
  private final Details spoiler = new Details("Spoiler:", new Paragraph());
  private final Paragraph description = new Paragraph();

  public Listing() {
    addClassName("centered-content");
    attributes.setOpened(true);
    spoiler.setOpened(false);
    var photos = new Div("Zdjęcia: TODO");
    var waypoints = new Div("Dodatkowe interesujące miejsa: TODO");
    add(
      title, fillBasicInfo(), attributes, description, spoiler, photos, waypoints,
      new RouterLink("GO BACK", MainView.class)
    );
  }

  private Div fillBasicInfo() {
    basicInfoContent.put("listingOwner", new Paragraph("Właściciel: "));
    basicInfoContent.put("type", new Paragraph("Typ: "));
    basicInfoContent.put("coords", new Paragraph("Współrzędne: "));
    basicInfoContent.put("state", new Paragraph("Status: "));
    basicInfoContent.put("size", new Paragraph("Wielkość: "));
    basicInfoContent.put("difficulty", new Paragraph("Trudność: "));
    var basicInfo = new Div();
    basicInfo.add(
      basicInfoContent.get("listingOwner"),
      basicInfoContent.get("type"),
      basicInfoContent.get("coords"),
      basicInfoContent.get("state"),
      basicInfoContent.get("size"),
      basicInfoContent.get("difficulty")
    );
    return basicInfo;
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
    title.setText(geocache.name + " - " + geocache.code);
    basicInfoContent.get("listingOwner").setText("Właściciel: " + geocache.owner);
    basicInfoContent.get("type").setText("Typ: " + geocache.type);
    basicInfoContent.get("coords").setText("Współrzędne: " + geocache.coordsVisible);
    basicInfoContent.get("state").setText("Status: " + geocache.state);
    basicInfoContent.get("size").setText("Wielkość: " + geocache.size);
    basicInfoContent.get("difficulty").setText("Trudność: " + geocache.difficulty);
    attributes.add(new UnorderedList(
      geocache.attributes.stream().map(ListItem::new).toArray(ListItem[]::new)
    ));
    description.setText(geocache.description);
    spoiler.add(new Paragraph(geocache.spoiler));
  }

}
