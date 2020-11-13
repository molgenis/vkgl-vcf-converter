package org.molgenis.vkgl.runner;

import org.molgenis.vkgl.Settings;
import org.molgenis.vkgl.converter.VkglVcfWriter;

public interface VkglVcfWriterFactory {
  VkglVcfWriter create(Settings settings);
}
