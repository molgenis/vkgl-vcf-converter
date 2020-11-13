package org.molgenis.vkgl.converter;

import java.io.StringReader;
import org.junit.jupiter.api.Test;

class VkglCsvReaderImplTest {

  @Test
  void read() {
    String csv = "\"id\",\"chromosome\",\"start\",\"stop\",\"ref\",\"alt\",\"gene\",\"c_dna\",\"transcript\",\"protein\",\"hgvs\",\"erasmus_link\",\"amc_link\",\"nki_link\",\"umcg_link\",\"lumc_link\",\"vumc_link\",\"radboud_mumc_link\",\"umcu_link\",\"amc\",\"erasmus\",\"lumc\",\"nki\",\"radboud_mumc\",\"umcg\",\"umcu\",\"vumc\",\"consensus_classification\",\"matches\",\"disease\",\"comments\",\"history\"";
    try (VkglCsvReaderImpl vkglCsvReader = new VkglCsvReaderImpl(new StringReader(csv))) {
      // FIXME implement
      throw new RuntimeException();
    }
  }
}
