package com.example.demo.geocaches;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
  public List<String> attributes;
  public String description;
  public String spoiler;

  public GeocacheEntity(String code, String name, String owner, String type, Double coordLat, Double coordLon, String coordsVisible, String state, String size, String difficulty, List<String> attributes, String description, String spoiler) {
    this.code = code;
    this.name = name;
    this.owner = owner;
    this.type = type;
    this.coordLat = coordLat;
    this.coordLon = coordLon;
    this.coordsVisible = coordsVisible;
    this.state = state;
    this.size = size;
    this.difficulty = difficulty;
    this.attributes = attributes;
    this.description = description;
    this.spoiler = spoiler;
  }

}
