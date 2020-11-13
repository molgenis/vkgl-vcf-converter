package org.molgenis.vkgl.converter.model;

public enum ConsensusClassification {
  CLASSIFIED_ONE_LAB("1LAB"),
  NO_CONSENSUS("NO"),
  LIKELY_BENIGN("LB"),
  VUS("VUS"),
  LIKELY_PATHOGENIC("LP");

  private final String id;

  ConsensusClassification(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
