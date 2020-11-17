package org.molgenis.vkgl.runner;

import static java.lang.String.format;

import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.molgenis.vkgl.Settings;
import org.molgenis.vkgl.WriterSettings;
import org.molgenis.vkgl.converter.VkglVcfWriter;
import org.molgenis.vkgl.converter.VkglVcfWriterImpl;
import org.springframework.stereotype.Component;

@Component
public class VkglVcfWriterFactoryImpl implements VkglVcfWriterFactory {

  @Override
  public VkglVcfWriter create(Settings settings) {
    WriterSettings writerSettings = settings.getWriterSettings();
    VariantContextWriter vcfWriter = createVcfWriter(writerSettings);
    return new VkglVcfWriterImpl(vcfWriter, settings.getAppSettings(),
        writerSettings.isWritePublic());
  }

  private static VariantContextWriter createVcfWriter(WriterSettings settings) {
    Path outputVcfPath = settings.getOutputVcfPath();
    if (settings.isOverwriteOutput()) {
      try {
        Files.deleteIfExists(outputVcfPath);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    } else if (Files.exists(outputVcfPath)) {
      throw new IllegalArgumentException(
          format("cannot create '%s' because it already exists.", outputVcfPath));
    }

    return new VariantContextWriterBuilder()
        .clearOptions()
        .setOutputFile(outputVcfPath.toFile())
        .build();
  }
}
