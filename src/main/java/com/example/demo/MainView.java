package com.example.demo;

import com.example.demo.geocaches.CreateListing;
import com.example.demo.geocaches.GeocacheRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PageTitle("Home - GeocachingWebApp")
public class MainView extends VerticalLayout {

  @Autowired
  private GreetService greetService;
  @Autowired
  private GeocacheRepository geocacheRepository;

  public MainView() {
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
    addClassName("centered-content");
    demoGreet();
    showMap();
    add(new RouterLink("Create new geocache listing", CreateListing.class));
  }

  private void demoGreet() {
    // Use TextField for standard text input
    TextField textField = new TextField("Your name");

    // Button click listeners can be defined as lambda expressions
    var greeting = new Paragraph();
    Button button = new Button("Say hello", e ->
      greeting.setText(greetService.greet(textField.getValue()))
    );

    // Theme variants give you predefined extra styles for components.
    // Example: Primary button is more prominent look.
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    // You can specify keyboard shortcuts for buttons.
    // Example: Pressing enter in this view clicks the Button.
    button.addClickShortcut(Key.ENTER);

    add(textField, button, greeting);
  }

  private void showMap() {
    // Let the view use 100% of the site
    setSizeFull();

    // Create the registry which is needed so that components can be reused and their methods invoked
    // Note: You normally don't need to invoke any methods of the registry and just hand it over to the components
    LComponentManagementRegistry reg = new LDefaultComponentManagementRegistry(this);

    // Create and add the MapContainer (which contains the map) to the UI
    var mapContainer = new MapContainer(reg);
    mapContainer.setSizeFull();
    add(mapContainer);

    LMap map = mapContainer.getlMap();

    // Add a (default) TileLayer so that we can see something on the map
    map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(reg));

    // Set what part of the world should be shown
    map.setView(new LLatLng(reg, 52.09232, 21.32782), 11);
    addGeocachesToMap(reg, map);
  }

  private void addGeocachesToMap(LComponentManagementRegistry reg, LMap map) {
    geocacheRepository.findAll().forEach(geocache -> {
      // Create a new marker
      new LMarker(reg, new LLatLng(reg, geocache.coordLat, geocache.coordLon))
        // Bind a popup which is displayed when clicking the marker
        .bindPopup("<a href='/listing/" + geocache.code + "'>" + geocache.name + " - " + geocache.code + "</a>")
        .addTo(map);
    });
  }

}
