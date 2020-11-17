package org.molgenis.vkgl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.util.ResourceUtils;

class AppIT {

  private static final Pattern HEADER_VERSION_PATTERN =
      Pattern.compile("^##VKGL_convertCommand=.*?$", Pattern.MULTILINE);
  private static final Pattern HEADER_COMMAND_PATTERN =
      Pattern.compile("^##VKGL_convertVersion=.*?$", Pattern.MULTILINE);

  @TempDir
  Path sharedTempDir;

  @Test
  void test() throws IOException {
    String inputFile = ResourceUtils.getFile("classpath:vkgl_consensus.csv").toString();
    String outputFile = sharedTempDir.resolve("vkgl_consensus.vcf").toString();

    String[] args = {"-i", inputFile, "-o", outputFile};
    SpringApplication.run(App.class, args);

    String outputVcf = Files.readString(Path.of(outputFile));

    // output differs every run (different tmp dir)
    outputVcf = HEADER_VERSION_PATTERN.matcher(outputVcf).replaceAll("##VKGL_convertVersion=");
    outputVcf = HEADER_COMMAND_PATTERN.matcher(outputVcf).replaceAll("##VKGL_convertCommand=");

    Path expectedOutputFile = ResourceUtils.getFile("classpath:vkgl_consensus.vcf").toPath();
    String expectedOutputVcf = Files.readString(expectedOutputFile).replaceAll("\\R", "\n");

    assertEquals(expectedOutputVcf, outputVcf);
  }

  @Test
  void testPublic() throws IOException {
    String inputFile = ResourceUtils.getFile("classpath:vkgl_consensus.csv").toString();
    String outputFile = sharedTempDir.resolve("vkgl_consensus_public.vcf").toString();

    String[] args = {"-i", inputFile, "-o", outputFile, "-p"};
    SpringApplication.run(App.class, args);

    String outputVcf = Files.readString(Path.of(outputFile));

    // output differs every run (different tmp dir)
    outputVcf = HEADER_VERSION_PATTERN.matcher(outputVcf).replaceAll("##VKGL_convertVersion=");
    outputVcf = HEADER_COMMAND_PATTERN.matcher(outputVcf).replaceAll("##VKGL_convertCommand=");

    Path expectedOutputFile = ResourceUtils.getFile("classpath:vkgl_consensus_public.vcf").toPath();
    String expectedOutputVcf = Files.readString(expectedOutputFile).replaceAll("\\R", "\n");

    assertEquals(expectedOutputVcf, outputVcf);
  }
}
