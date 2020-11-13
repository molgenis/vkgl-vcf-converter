package org.molgenis.vkgl.converter.model;

import static java.lang.String.format;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class TextToConsensusClassificationConverter extends AbstractBeanField<Classification, Object> {

  @Override
  public Object convert(String value) throws CsvDataTypeMismatchException {
    ConsensusClassification consensusClassification;
    switch (value) {
      case "Classified by one lab":
        consensusClassification = ConsensusClassification.CLASSIFIED_ONE_LAB;
        break;
      case "No consensus":
        consensusClassification = ConsensusClassification.NO_CONSENSUS;
        break;
      case "(Likely) benign":
        consensusClassification = ConsensusClassification.LIKELY_BENIGN;
        break;
      case "VUS":
        consensusClassification = ConsensusClassification.VUS;
        break;
      case "(Likely) pathogenic":
        consensusClassification = ConsensusClassification.LIKELY_PATHOGENIC;
        break;
      default:
        throw new CsvDataTypeMismatchException(format("invalid classification '%s'", value));
    }
    return consensusClassification;
  }
}
