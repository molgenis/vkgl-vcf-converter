package org.molgenis.vkgl.converter;

import java.util.List;
import org.molgenis.vkgl.converter.model.ConsensusRecord;

public interface VkglCsvReader extends AutoCloseable {
  List<ConsensusRecord> read();
}
