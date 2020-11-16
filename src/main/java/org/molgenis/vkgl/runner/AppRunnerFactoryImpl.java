package org.molgenis.vkgl.runner;

import static java.util.Objects.requireNonNull;

import org.molgenis.vkgl.Settings;
import org.molgenis.vkgl.converter.VkglCsvReader;
import org.molgenis.vkgl.converter.VkglVcfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class AppRunnerFactoryImpl implements AppRunnerFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppRunnerFactoryImpl.class);

  private final VkglCsvReaderFactory vkglCsvReaderFactory;
  private final VkglVcfWriterFactory vkglVcfWriterFactory;

  AppRunnerFactoryImpl(
      VkglCsvReaderFactory vkglCsvReaderFactory,
      VkglVcfWriterFactory vkglVcfWriterFactory) {
    this.vkglCsvReaderFactory = requireNonNull(vkglCsvReaderFactory);
    this.vkglVcfWriterFactory = requireNonNull(vkglVcfWriterFactory);
  }

  // Suppress 'Resources should be closed'
  @SuppressWarnings("java:S2095")
  @Override
  public AppRunner create(Settings settings) {
    VkglCsvReader vkglCsvReader = vkglCsvReaderFactory.create(settings);
    try {
      VkglVcfWriter vkglVcfWriter = vkglVcfWriterFactory.create(settings);
      return new AppRunnerImpl(vkglCsvReader,
          vkglVcfWriter);
    } catch (Exception e) {
      try {
        vkglCsvReader.close();
      } catch (Exception closeException) {
        LOGGER.warn("error closing csv reader", closeException);
      }
      throw e;
    }
  }
}
