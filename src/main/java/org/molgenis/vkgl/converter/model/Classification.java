package org.molgenis.vkgl.converter.model;

public enum Classification {
  BENIGN("B"),
  LIKELY_BENIGN("LB"),
  VUS("VUS"),
  LIKELY_PATHOGENIC("LP"),
  PATHOGENIC("P");

  private final String id;

  Classification(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
