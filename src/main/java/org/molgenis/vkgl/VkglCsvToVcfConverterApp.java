package org.molgenis.vkgl;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class VkglCsvToVcfConverterApp {
  public static void main(String[] args) {
    CommandLineParser commandLineParser = new DefaultParser();

    Options options = new Options();
    options.addOption(
        Option.builder("i")
            .longOpt("input")
            .required()
            .hasArg(true)
            .desc("Input VKGL export (.csv).")
            .build());
    options.addOption(
        Option.builder("o")
            .longOpt("output")
            .hasArg(true)
            .desc("Output VKGL export (.vcf).")
            .build());
    options.addOption(Option.builder("h").longOpt("help").desc("Show help message.").build());
    options.addOption(
        Option.builder("f")
            .longOpt("force")
            .desc("Override the output file if it already exists.")
            .build());
    options.addOption(
        Option.builder("v").longOpt("verbose").desc("Print debug information.").build());

    CommandLine commandLine;
    try {
      // parse the command line arguments
      commandLine = commandLineParser.parse(options, args);
    } catch (ParseException exp) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("java -jar vkgl-vcf-converter.jar", options);
      return;
    }

    String inputFile = commandLine.getOptionValue("i");
    if (!new File(inputFile).exists()) {
      System.err.println(inputFile + " does not exist");
    }
    String outputFile;
    if (commandLine.hasOption("o")) {
      outputFile = commandLine.getOptionValue("o");
      if (new File(outputFile).exists() && commandLine.hasOption("f")) {
        boolean deleteOk = new File(outputFile).delete();
        if (!deleteOk) {
          System.err.println("unable to delete " + outputFile);
        }
      }
    } else {
      outputFile = inputFile.replace(".csv", ".vcf");
    }
    try {
      new VkglCsvToVcfConverter().convert(inputFile, outputFile);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
