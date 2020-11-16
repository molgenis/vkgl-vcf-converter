package org.molgenis.vkgl.converter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ConsensusRecordTest {

  @Test
  void getClassificationBenign() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.LIKELY_BENIGN)
        .amcClassification(Classification.BENIGN).erasmusClassification(Classification.BENIGN)
        .build();

    assertEquals(Classification.BENIGN, consensusRecord.getClassification());
  }

  @Test
  void getClassificationLikelyBenign() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.LIKELY_BENIGN)
        .lumcClassification(Classification.BENIGN).nkiClassification(Classification.LIKELY_BENIGN)
        .build();

    assertEquals(Classification.LIKELY_BENIGN, consensusRecord.getClassification());
  }

  @Test
  void getClassificationVus() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.VUS).build();

    assertEquals(Classification.VUS, consensusRecord.getClassification());
  }

  @Test
  void getClassificationLikelyPathogenic() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.LIKELY_PATHOGENIC)
        .radboudMumcClassification(Classification.PATHOGENIC)
        .umcgClassification(Classification.LIKELY_PATHOGENIC).build();

    assertEquals(Classification.LIKELY_PATHOGENIC, consensusRecord.getClassification());
  }

  @Test
  void getClassificationPathogenic() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.LIKELY_PATHOGENIC)
        .umcuClassification(Classification.PATHOGENIC).vumcClassification(Classification.PATHOGENIC)
        .build();

    assertEquals(Classification.PATHOGENIC, consensusRecord.getClassification());
  }

  @Test
  void getClassificationOneLab() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.CLASSIFIED_BY_ONE_LAB)
        .amcClassification(Classification.BENIGN)
        .build();

    assertEquals(Classification.BENIGN, consensusRecord.getClassification());
  }


  @Test
  void getClassificationNoConsensus() {
    ConsensusRecord consensusRecord = ConsensusRecord.builder()
        .consensusClassification(ConsensusClassification.NO_CONSENSUS)
        .build();

    assertNull(consensusRecord.getClassification());
  }
}