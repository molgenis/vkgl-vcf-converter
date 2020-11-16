package org.molgenis.vkgl.converter.model;

import static java.lang.String.format;
import static org.molgenis.vkgl.converter.model.Classification.BENIGN;
import static org.molgenis.vkgl.converter.model.Classification.LIKELY_BENIGN;
import static org.molgenis.vkgl.converter.model.Classification.LIKELY_PATHOGENIC;
import static org.molgenis.vkgl.converter.model.Classification.PATHOGENIC;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import java.util.EnumSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsensusRecord {

  @CsvBindByName(column = "chromosome", required = true)
  String chromosome;

  @CsvBindByName(column = "start", required = true)
  int pos;

  @CsvBindByName(column = "ref", required = true)
  String ref;

  @CsvBindByName(column = "alt", required = true)
  String alt;

  @CsvCustomBindByName(
      column = "amc",
      converter = TextToClassificationConverter.class)
  Classification amcClassification;

  @CsvCustomBindByName(
      column = "erasmus",
      converter = TextToClassificationConverter.class)
  Classification erasmusClassification;

  @CsvCustomBindByName(
      column = "lumc",
      converter = TextToClassificationConverter.class)
  Classification lumcClassification;

  @CsvCustomBindByName(
      column = "nki",
      converter = TextToClassificationConverter.class)
  Classification nkiClassification;

  @CsvCustomBindByName(
      column = "radboud_mumc",
      converter = TextToClassificationConverter.class)
  Classification radboudMumcClassification;

  @CsvCustomBindByName(
      column = "umcg",
      converter = TextToClassificationConverter.class)
  Classification umcgClassification;

  @CsvCustomBindByName(
      column = "umcu",
      converter = TextToClassificationConverter.class)
  Classification umcuClassification;

  @CsvCustomBindByName(
      column = "vumc",
      converter = TextToClassificationConverter.class)
  Classification vumcClassification;

  @CsvCustomBindByName(
      column = "consensus_classification",
      converter = TextToConsensusClassificationConverter.class)
  ConsensusClassification consensusClassification;

  @CsvBindByName(column = "matches")
  Integer matches;

  public Classification getClassification() {
    Classification classification;
    switch (consensusClassification) {
      case LIKELY_BENIGN:
        classification = getClassificationSet().equals(EnumSet.of(BENIGN)) ? BENIGN : LIKELY_BENIGN;
        break;
      case VUS:
        classification = Classification.VUS;
        break;
      case LIKELY_PATHOGENIC:
        classification =
            getClassificationSet().equals(EnumSet.of(PATHOGENIC)) ? PATHOGENIC : LIKELY_PATHOGENIC;
        break;
      case CLASSIFIED_BY_ONE_LAB:
        classification = getClassificationSet().iterator().next();
        break;
      case NO_CONSENSUS:
        classification = null;
        break;
      default:
        throw new IllegalArgumentException(
            format("invalid classification '%s'", consensusClassification));
    }
    return classification;
  }

  private Set<Classification> getClassificationSet() {
    EnumSet<Classification> classifications = EnumSet.noneOf(Classification.class);
    addClassification(classifications, amcClassification);
    addClassification(classifications, erasmusClassification);
    addClassification(classifications, lumcClassification);
    addClassification(classifications, nkiClassification);
    addClassification(classifications, radboudMumcClassification);
    addClassification(classifications, umcgClassification);
    addClassification(classifications, umcuClassification);
    addClassification(classifications, vumcClassification);
    return classifications;
  }

  private static void addClassification(EnumSet<Classification> classifications,
      Classification classification) {
    if (classification != null) {
      classifications.add(classification);
    }
  }
}
