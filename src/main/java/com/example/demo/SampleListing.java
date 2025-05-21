package com.example.demo;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("/sample_listing")
@PageTitle("Ambona na wzniesieniu - GeocachingWebApp")
public class SampleListing extends VerticalLayout {

  public SampleListing() {
    addClassName("centered-content");
    var title = new H1("Ambona na wzniesieniu - OPA04C");

    var basicInfo = new Div();
    var listingOwner = new Paragraph("Właściciel: Michu4000");
    var type = new Paragraph("Typ: Skrzynka tradycyjna");
    var coords = new Paragraph("Współrzędne: N 52° 05.539' E 21° 19.669'");
    var state = new Paragraph("Status: Aktywna");
    var size = new Paragraph("Wielkość: Mała");
    var difficulty = new Paragraph("Trudność: 2/5");
    basicInfo.add(listingOwner, type, coords, state, size, difficulty);

    var attributes = new Details("Atrybuty:", new UnorderedList(
      new ListItem("Przyjazna dla rodzin z dziećmi"),
      new ListItem("Można dojechać rowerem"),
      new ListItem("Na łonie natury"),
      new ListItem("Można dojechać rowerem")
    ));
    attributes.setOpened(true);

    var details = new Paragraph("""
      Niewielkie wzniesienie w lesie, obok bagienko i całkiem dobra droga dojazdowa.
      Ładne ciche miejsce z amboną, dobry spot dla fanów obserwacji zwierząt (często można spotkać tu np. łosie).
      
      Skrzynka wymaga jako-takiej ogólnej sprawności fizycznej, gdyż do jej podjęcia konieczna jest (nietrudna) wspinaczka.
      Proszę nie zdejmować pojemnika, tylko wyciągnąć zawartość.
      
      Powodzenia ;)""");

    var spoiler = new Details("Spoiler:", new Paragraph("Ukryta na wysokości ok. 3-4m, na pokręconej sośnie."));
    spoiler.setOpened(false);

    var photos = new Div("Zdjęcia: TODO");
    var waypoints = new Div("Dodatkowe interesujące miejsa: TODO");

    add(title, basicInfo, attributes, details, spoiler, photos, waypoints, new RouterLink("GO BACK", MainView.class));
  }

}
