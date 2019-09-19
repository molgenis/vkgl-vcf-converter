package org.molgenis.vkgl.converter;

import java.util.List;
import org.molgenis.vkgl.converter.model.ConsensusRecord;

public interface VkglVcfWriter extends AutoCloseable {

  void writeHeader();

  void write(List<ConsensusRecord> consensusRecords);
}
