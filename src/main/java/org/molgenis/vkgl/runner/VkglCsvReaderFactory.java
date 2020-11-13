package org.molgenis.vkgl.runner;

import org.molgenis.vkgl.Settings;
import org.molgenis.vkgl.converter.VkglCsvReader;

public interface VkglCsvReaderFactory {
  VkglCsvReader create(Settings settings);
}
