package com.example.demo.geocaches;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document
public class GeocacheEntity {

  @Id
  public String code;
  public String name;
  public String owner;
  public String type;
  public Double coordLat;
  public Double coordLon;
  public String coordsVisible;
  public String state;
  public String size;
  public String difficulty;
  public Set<String> attributes;
  public String description;
  public String spoiler;
  public List<GeocacheLogEntity> logs = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getCoordLat() {
    return coordLat;
  }

  public void setCoordLat(Double coordLat) {
    this.coordLat = coordLat;
  }

  public Double getCoordLon() {
    return coordLon;
  }

  public void setCoordLon(Double coordLon) {
    this.coordLon = coordLon;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public Set<String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Set<String> attributes) {
    this.attributes = attributes;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSpoiler() {
    return spoiler;
  }

  public void setSpoiler(String spoiler) {
    this.spoiler = spoiler;
  }

}
