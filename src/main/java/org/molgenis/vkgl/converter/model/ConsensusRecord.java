package org.molgenis.vkgl.converter.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
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
}
