package org.molgenis.vkgl.converter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextToConsensusClassificationConverterTest {

  private TextToConsensusClassificationConverter textToConsensusClassificationConverter;

  @BeforeEach
  void setUp() {
    textToConsensusClassificationConverter = new TextToConsensusClassificationConverter();
  }

  @Test
  void convertLikelyBenign() throws CsvDataTypeMismatchException {
    assertEquals(ConsensusClassification.LIKELY_BENIGN,
        textToConsensusClassificationConverter.convert("(Likely) benign"));
  }

  @Test
  void convertVus() throws CsvDataTypeMismatchException {
    assertEquals(ConsensusClassification.VUS,
        textToConsensusClassificationConverter.convert("VUS"));
  }

  @Test
  void convertLikelyPathogenic() throws CsvDataTypeMismatchException {
    assertEquals(ConsensusClassification.LIKELY_PATHOGENIC,
        textToConsensusClassificationConverter.convert("(Likely) pathogenic"));
  }

  @Test
  void convertClassifiedByOneLab() throws CsvDataTypeMismatchException {
    assertEquals(ConsensusClassification.CLASSIFIED_BY_ONE_LAB,
        textToConsensusClassificationConverter.convert("Classified by one lab"));
  }

  @Test
  void convertNoConsensus() throws CsvDataTypeMismatchException {
    assertEquals(ConsensusClassification.NO_CONSENSUS,
        textToConsensusClassificationConverter.convert("No consensus"));
  }

  @Test
  void convertOppositeClassifications() throws CsvDataTypeMismatchException {
    assertEquals(ConsensusClassification.OPPOSITE_CLASSIFICATIONS,
        textToConsensusClassificationConverter.convert("Opposite classifications"));
  }

  @Test
  void convertUnknownConsensusClassification() {
    assertThrows(CsvDataTypeMismatchException.class,
        () -> textToConsensusClassificationConverter.convert("Unknown"));
  }
}