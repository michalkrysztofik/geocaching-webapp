package com.example.demo.geocaches;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
