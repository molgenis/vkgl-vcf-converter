package org.molgenis.vkgl.converter.model;

public enum ConsensusClassification {
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
