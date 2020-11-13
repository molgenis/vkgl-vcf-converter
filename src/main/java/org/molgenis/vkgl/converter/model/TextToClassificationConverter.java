package org.molgenis.vkgl.converter.model;

import static java.lang.String.format;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class TextToClassificationConverter extends AbstractBeanField<Classification, Object> {

  @Override
  public Object convert(String value) throws CsvDataTypeMismatchException {
    if(value == null || value.isEmpty()) {
      return null;
    }

    Classification classification;
    switch (value) {
      case "Benign":
        classification = Classification.BENIGN;
        break;
      case "Likely benign":
        classification = Classification.LIKELY_BENIGN;
        break;
      case "VUS":
        classification = Classification.VUS;
        break;
      case "Likely pathogenic":
        classification = Classification.LIKELY_PATHOGENIC;
        break;
      case "Pathogenic":
        classification = Classification.PATHOGENIC;
        break;
      default:
        throw new CsvDataTypeMismatchException(format("invalid classification '%s'", value));
    }
    return classification;
  }
}
