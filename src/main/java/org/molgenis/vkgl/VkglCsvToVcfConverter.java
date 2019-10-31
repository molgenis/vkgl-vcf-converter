package org.molgenis.vkgl;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class VkglCsvToVcfConverter {

  void convert(String inputFile, String outputFile) throws IOException {

    try (BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), UTF_8))) {
      writer.write(
          "##fileformat=VCFv4.3\n"
              + "##fileDate=20190822\n"
              + "##contig=<ID=1,length=249250621,assembly=b37>\n"
              + "##contig=<ID=2,length=243199373,assembly=b37>\n"
              + "##contig=<ID=3,length=198022430,assembly=b37>\n"
              + "##contig=<ID=4,length=191154276,assembly=b37>\n"
              + "##contig=<ID=5,length=180915260,assembly=b37>\n"
              + "##contig=<ID=6,length=171115067,assembly=b37>\n"
              + "##contig=<ID=7,length=159138663,assembly=b37>\n"
              + "##contig=<ID=8,length=146364022,assembly=b37>\n"
              + "##contig=<ID=9,length=141213431,assembly=b37>\n"
              + "##contig=<ID=10,length=135534747,assembly=b37>\n"
              + "##contig=<ID=11,length=135006516,assembly=b37>\n"
              + "##contig=<ID=12,length=133851895,assembly=b37>\n"
              + "##contig=<ID=13,length=115169878,assembly=b37>\n"
              + "##contig=<ID=14,length=107349540,assembly=b37>\n"
              + "##contig=<ID=15,length=102531392,assembly=b37>\n"
              + "##contig=<ID=16,length=90354753,assembly=b37>\n"
              + "##contig=<ID=17,length=81195210,assembly=b37>\n"
              + "##contig=<ID=18,length=78077248,assembly=b37>\n"
              + "##contig=<ID=19,length=59128983,assembly=b37>\n"
              + "##contig=<ID=20,length=63025520,assembly=b37>\n"
              + "##contig=<ID=21,length=48129895,assembly=b37>\n"
              + "##contig=<ID=22,length=51304566,assembly=b37>\n"
              + "##contig=<ID=X,length=155270560,assembly=b37>\n"
              + "##contig=<ID=Y,length=59373566,assembly=b37>\n"
              + "##contig=<ID=MT,length=16569,assembly=b37>\n"
              + "##INFO=<ID=VKGL_CL,Number=1,Type=String,Description=\"Clinical significance: LB (likely benign), VUS (unknown significance), LP (likely pathogenic)\">\n"
              + "##INFO=<ID=VKGL_NR,Number=1,Type=Integer,Description=\"Number of supporting labs\">\n"
              + "#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");

      List<String[]> lines;
      try (CSVReader csvReader =
          new CSVReader(Files.newBufferedReader(Paths.get(inputFile), UTF_8))) {
        lines = csvReader.readAll();
      }
      validateHeader(inputFile, lines);
      lines = lines.subList(1, lines.size());
      sortVariants(lines);
      writeLines(writer, lines);
    }
  }

  private void validateHeader(String inputFile, List<String[]> lines) throws IOException {
    if (lines.isEmpty()) {
      throw new IOException(inputFile + " is empty");
    }
    String[] headerTokens = lines.get(0);
    if (!Arrays.equals(
        headerTokens,
        new String[]{
            "ID",
            "label",
            "chromosome",
            "start",
            "stop",
            "ref",
            "alt",
            "c_notation",
            "p_notation",
            "transcript",
            "hgvs",
            "gene",
            "classification",
            "support"
        })) {
      throw new IOException(inputFile + " header invalid");
    }
  }

  private void writeLines(BufferedWriter writer, List<String[]> lines) throws IOException {
    for (String[] tokens : lines) {
      String chromosome = tokens[2];
      String start = tokens[3];
      String ref = tokens[5];
      String alt = tokens[6];
      String classification = tokens[12];
      String support = tokens[13];
      writer.write(chromosome);
      writer.write('\t');
      writer.write(start);
      writer.write('\t');
      writer.write('.');
      writer.write('\t');
      writer.write(ref);
      writer.write('\t');
      writer.write(alt);
      writer.write('\t');
      writer.write('.');
      writer.write('\t');
      writer.write('.');
      writer.write('\t');
      writer.write("VKGL_CL=" + classification + ";VKGL_NR=" + support.split(" ")[0]);
      writer.write('\n');
    }
  }

  private static void sortVariants(List<String[]> lines) {
    lines.sort(
        (tokens0, tokens1) -> {
          String chrom0 = tokens0[2];
          String chrom1 = tokens1[2];
          switch (chrom0) {
            case "X":
              switch (chrom1) {
                case "X":
                  return Integer.compare(
                      Integer.parseInt(tokens0[3]), Integer.parseInt(tokens1[3]));
                case "Y":
                  return -1;
                case "MT":
                  return -1;
                default:
                  return 1;
              }
            case "Y":
              switch (chrom1) {
                case "Y":
                  return Integer.compare(
                      Integer.parseInt(tokens0[3]), Integer.parseInt(tokens1[3]));
                case "MT":
                  return -1;
                default:
                  return 1;
              }
            case "MT":
              return "MT".equals(chrom1)
                  ? Integer.compare(Integer.parseInt(tokens0[3]), Integer.parseInt(tokens1[3]))
                  : 1;
            default:
              switch (chrom1) {
                case "X":
                case "Y":
                case "MT":
                  return -1;
                default:
                  int compare = Integer.compare(Integer.parseInt(chrom0), Integer.parseInt(chrom1));
                  return compare != 0
                      ? compare
                      : Integer.compare(Integer.parseInt(tokens0[3]), Integer.parseInt(tokens1[3]));
              }
          }
        });
  }
}
