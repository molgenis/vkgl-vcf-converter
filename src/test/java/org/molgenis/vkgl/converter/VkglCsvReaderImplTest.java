package org.molgenis.vkgl.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.molgenis.vkgl.converter.model.Classification;
import org.molgenis.vkgl.converter.model.ConsensusClassification;
import org.molgenis.vkgl.converter.model.ConsensusRecord;

class VkglCsvReaderImplTest {

  @Test
  void read() {
    String csv =
        "\"id\",\"chromosome\",\"start\",\"stop\",\"ref\",\"alt\",\"gene\",\"c_dna\",\"transcript\",\"protein\",\"hgvs\",\"erasmus_link\",\"amc_link\",\"nki_link\",\"umcg_link\",\"lumc_link\",\"vumc_link\",\"radboud_mumc_link\",\"umcu_link\",\"amc\",\"erasmus\",\"lumc\",\"nki\",\"radboud_mumc\",\"umcg\",\"umcu\",\"vumc\",\"consensus_classification\",\"matches\",\"disease\",\"comments\",\"history\"\n"
            + ",\"1\",123,,\"C\",\"T\",,,,,,,,,,,,,,,,,,,,,\"Benign\",\"Classified by one lab\",\"1\",,,\n"
            + ",\"1\",124,,\"T\",\"C\",,,,,,,,,,,,,,,,,,,\"Pathogenic\",\"Benign\",,\"No consensus\",,,,";

    ConsensusRecord consensusRecord0 = ConsensusRecord.builder().chromosome("1").pos(123).ref("C")
        .alt("T").vumcClassification(
            Classification.BENIGN)
        .consensusClassification(ConsensusClassification.CLASSIFIED_BY_ONE_LAB).matches(1).build();
    ConsensusRecord consensusRecord1 = ConsensusRecord.builder().chromosome("1").pos(124).ref("T")
        .alt("C").umcgClassification(Classification.PATHOGENIC)
        .umcuClassification(Classification.BENIGN)
        .consensusClassification(ConsensusClassification.NO_CONSENSUS).build();
    try (VkglCsvReaderImpl vkglCsvReader = new VkglCsvReaderImpl(new StringReader(csv))) {
      assertEquals(List.of(consensusRecord0, consensusRecord1), vkglCsvReader.read());
    }
  }

  @Test
  void readException() {
    String csv =
        "\"id\",\"chromosome\",\"start\",\"stop\",\"ref\",\"alt\",\"gene\",\"c_dna\",\"transcript\",\"protein\",\"hgvs\",\"erasmus_link\",\"amc_link\",\"nki_link\",\"umcg_link\",\"lumc_link\",\"vumc_link\",\"radboud_mumc_link\",\"umcu_link\",\"amc\",\"erasmus\",\"lumc\",\"nki\",\"radboud_mumc\",\"umcg\",\"umcu\",\"vumc\",\"consensus_classification\",\"matches\",\"disease\",\"comments\",\"history\"\n"
            + ",\"1\",123,,\"C\",\"T\",,,,,,,,,,,,,,,,,,,,,\"Benign\",\"Classified by two labs\",\"1\",,,\n"
            + ",\"1\",124,,\"T\",\"C\",,,,,,,,,,,,,,,,,,,\"Pathogenic\",\"Benign\",,\"No consensus\",,,,";
    ConsensusRecord consensusRecord = ConsensusRecord.builder().chromosome("1").pos(124).ref("T")
        .alt("C").umcgClassification(Classification.PATHOGENIC)
        .umcuClassification(Classification.BENIGN)
        .consensusClassification(ConsensusClassification.NO_CONSENSUS).build();
    try (VkglCsvReaderImpl vkglCsvReader = new VkglCsvReaderImpl(new StringReader(csv))) {
      assertEquals(List.of(consensusRecord), vkglCsvReader.read());
    }
  }
}
