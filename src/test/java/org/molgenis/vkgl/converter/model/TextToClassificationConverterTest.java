package org.molgenis.vkgl.converter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.molgenis.vkgl.converter.model.Classification.BENIGN;
import static org.molgenis.vkgl.converter.model.Classification.LIKELY_BENIGN;
import static org.molgenis.vkgl.converter.model.Classification.LIKELY_PATHOGENIC;
import static org.molgenis.vkgl.converter.model.Classification.PATHOGENIC;
import static org.molgenis.vkgl.converter.model.Classification.VUS;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextToClassificationConverterTest {

  private TextToClassificationConverter textToClassificationConverter;

  @BeforeEach
  void setUp() {
    textToClassificationConverter = new TextToClassificationConverter();
  }

  @Test
  void convertNull() throws CsvDataTypeMismatchException {
    assertNull(textToClassificationConverter.convert(null));
  }

  @Test
  void convertEmpty() throws CsvDataTypeMismatchException {
    assertNull(textToClassificationConverter.convert(""));
  }

  @Test
  void convertBenign() throws CsvDataTypeMismatchException {
    assertEquals(BENIGN, textToClassificationConverter.convert("Benign"));
  }

  @Test
  void convertLikelyBenign() throws CsvDataTypeMismatchException {
    assertEquals(LIKELY_BENIGN, textToClassificationConverter.convert("Likely benign"));
  }

  @Test
  void convertVus() throws CsvDataTypeMismatchException {
    assertEquals(VUS, textToClassificationConverter.convert("VUS"));
  }

  @Test
  void convertLikelyPathogenic() throws CsvDataTypeMismatchException {
    assertEquals(LIKELY_PATHOGENIC, textToClassificationConverter.convert("Likely pathogenic"));
  }

  @Test
  void convertPathogenic() throws CsvDataTypeMismatchException {
    assertEquals(PATHOGENIC, textToClassificationConverter.convert("Pathogenic"));
  }

  @Test
  void convertUnknownClassification() {
    assertThrows(CsvDataTypeMismatchException.class,
        () -> textToClassificationConverter.convert("Unknown"));
  }
}