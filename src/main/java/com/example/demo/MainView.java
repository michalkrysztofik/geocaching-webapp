package com.example.demo;

import com.example.demo.geocaches.CreateListingView;
import com.example.demo.geocaches.GeocacheRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

@Route("")
@PermitAll
@PageTitle("Home - GeocachingWebApp")
public class MainView extends VerticalLayout {

  @Autowired
  private GeocacheRepository geocacheRepository;

  public MainView() {
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
    addClassName("centered-content");
    showMap();
    add(new RouterLink("Create new geocache listing", CreateListingView.class));
    add(createLogOutButton());
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
    geocacheRepository.findAll().forEach(geocache ->
      // Create a new marker
      new LMarker(reg, new LLatLng(reg, geocache.coordLat, geocache.coordLon))
        // Bind a popup which is displayed when clicking the marker
        .bindPopup("<a href='/listing/" + geocache.code + "'>" + geocache.name + " - " + geocache.code + "</a>")
        .addTo(map)
    );
  }

  private Button createLogOutButton() {
    return new Button("Logout", click -> {
      UI.getCurrent().getPage().setLocation("/");
      SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
      logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    });
  }

}
