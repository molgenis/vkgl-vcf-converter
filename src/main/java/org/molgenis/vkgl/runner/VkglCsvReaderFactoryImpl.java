package org.molgenis.vkgl.runner;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import org.molgenis.vkgl.Settings;
import org.molgenis.vkgl.converter.VkglCsvReader;
import org.molgenis.vkgl.converter.VkglCsvReaderImpl;
import org.springframework.stereotype.Component;

@Component
public class VkglCsvReaderFactoryImpl implements VkglCsvReaderFactory {

  @Override
  public VkglCsvReader create(Settings settings) {
    try {
      return new VkglCsvReaderImpl(Files.newBufferedReader(settings.getInputVkglConsensusCsvPath(), UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
