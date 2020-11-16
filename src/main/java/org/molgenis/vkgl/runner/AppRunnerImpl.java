package org.molgenis.vkgl.runner;

import static java.util.Objects.requireNonNull;

import org.molgenis.vkgl.converter.VkglCsvReader;
import org.molgenis.vkgl.converter.VkglVcfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AppRunnerImpl implements AppRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppRunnerImpl.class);

  private final VkglCsvReader vkglCsvReader;
  private final VkglVcfWriter vkglVcfWriter;

  AppRunnerImpl(VkglCsvReader vkglCsvReader, VkglVcfWriter vkglVcfWriter) {
    this.vkglCsvReader = requireNonNull(vkglCsvReader);
    this.vkglVcfWriter = requireNonNull(vkglVcfWriter);
  }

  public void run() {
    LOGGER.info("converting managed variant list to vcf...");
    vkglVcfWriter.writeHeader();
    vkglVcfWriter.write(vkglCsvReader.read());
    LOGGER.info("done");
  }

  @Override
  public void close() {
    try {
      vkglVcfWriter.close();
    } catch (Exception e) {
      LOGGER.error("error closing writer", e);
    }
    try {
      vkglCsvReader.close();
    } catch (Exception e) {
      LOGGER.error("error closing reader", e);
    }
  }
}
