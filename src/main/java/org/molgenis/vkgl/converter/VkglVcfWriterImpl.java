package org.molgenis.vkgl.converter;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.VariantContextBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFContigHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;
import htsjdk.variant.vcf.VCFHeaderLineCount;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFHeaderVersion;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.util.LinkedHashMap;
import java.util.List;
import org.molgenis.vkgl.AppSettings;
import org.molgenis.vkgl.converter.model.Classification;
import org.molgenis.vkgl.converter.model.ConsensusClassification;
import org.molgenis.vkgl.converter.model.ConsensusRecord;

public class VkglVcfWriterImpl implements VkglVcfWriter {

  private static final String CONTIG_ID = "ID";
  private static final String CONTIG_LENGTH = "length";
  private static final String CONTIG_ASSEMBLY = "assembly";
  private static final String ASSEMBLY = "b37";
  private static final String HEADER_MVL_VERSION = "VKGL_convertVersion";
  private static final String HEADER_MVL_ARGS = "VKGL_convertCommand";
  private static final String INFO_VKGL_CL = "VKGL_CL";
  private static final String INFO_VKGL_CL_DESC =
      "Clinical significance: LB (likely benign), VUS (unknown significance), LP (likely pathogenic)";
  private static final String INFO_VKGL_NR = "VKGL_NR";
  private static final String INFO_VKGL_NR_DESC =
      "Number of supporting labs";
  private static final String INFO_AMC = "AMC";
  private static final String INFO_AMC_DESC = "Amsterdam UMC clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_EMC = "EMC";
  private static final String INFO_EMC_DESC = "Erasmus MC clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_LUMC = "LUMC";
  private static final String INFO_LUMC_DESC = "Leiden University Medical Center clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_NKI = "NKI";
  private static final String INFO_NKI_DESC = "Netherlands Cancer Institute clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_RMMC = "RMMC";
  private static final String INFO_RMMC_DESC = "Radboud UMC / Maastricht UMC+ clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_UMCG = "UMCG";
  private static final String INFO_UMCG_DESC = "University Medical Center Groningen clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_UMCU = "UMCU";
  private static final String INFO_UMCU_DESC = "Utrecht MC clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";
  private static final String INFO_VUMC = "VUMC";
  private static final String INFO_VUMC_DESC = "VU University Medical Center clinical significance: B (benign), LB (likely benign), VUS (unknown significance), LP (likely pathogenic), P (pathogenic)";

  private final VariantContextWriter vcfWriter;
  private final AppSettings appSettings;
  private final VCFHeader vcfHeader;

  public VkglVcfWriterImpl(VariantContextWriter vcfWriter, AppSettings appSettings) {
    this.vcfWriter = requireNonNull(vcfWriter);
    this.appSettings = requireNonNull(appSettings);
    this.vcfHeader = createVcfHeader();
  }

  @Override
  public void writeHeader() {
    vcfWriter.writeHeader(vcfHeader);
  }

  @Override
  public void write(List<ConsensusRecord> consensusRecords) {
    if (consensusRecords.isEmpty()) {
      return;
    }

    List<VariantContext> variantContexts = convert(consensusRecords);
    sort(variantContexts);
    variantContexts.forEach(vcfWriter::add);
  }

  private List<VariantContext> convert(List<ConsensusRecord> consensusRecords) {
    return consensusRecords.stream().map(this::convert).collect(toList());
  }

  private void sort(List<VariantContext> variantContexts) {
    variantContexts.sort(vcfHeader.getVCFRecordComparator());
  }

  private VariantContext convert(ConsensusRecord consensusRecord) {
    int start = consensusRecord.getPos();

    Allele refAllele = Allele.create(consensusRecord.getRef(), true);
    Allele altAllele = Allele.create(consensusRecord.getAlt(), false);
    List<Allele> alleles = List.of(refAllele, altAllele);

    VariantContextBuilder variantContextBuilder = new VariantContextBuilder()
        .chr(consensusRecord.getChromosome())
        .start(start)
        .computeEndFromAlleles(alleles, start)
        .alleles(alleles);
    ConsensusClassification consensusClassification = consensusRecord.getConsensusClassification();
    if (consensusClassification != null) {
      variantContextBuilder
          .attribute(INFO_VKGL_CL, List.of(consensusRecord.getConsensusClassification().getId()));
      variantContextBuilder
          .attribute(INFO_VKGL_NR, List.of(consensusRecord.getMatches()));
    }
    Classification amcClassification = consensusRecord.getAmcClassification();
    if(amcClassification != null) {
      variantContextBuilder
          .attribute(INFO_AMC, List.of(amcClassification.getId()));
    }
    Classification emcClassification = consensusRecord.getErasmusClassification();
    if(emcClassification != null) {
      variantContextBuilder
          .attribute(INFO_EMC, List.of(emcClassification.getId()));
    }
    Classification lumcClassification = consensusRecord.getLumcClassification();
    if(lumcClassification != null) {
      variantContextBuilder
          .attribute(INFO_LUMC, List.of(lumcClassification.getId()));
    }
    Classification nkiClassification = consensusRecord.getNkiClassification();
    if(nkiClassification != null) {
      variantContextBuilder
          .attribute(INFO_NKI, List.of(nkiClassification.getId()));
    }
    Classification radboudMumcClassification = consensusRecord.getRadboudMumcClassification();
    if(radboudMumcClassification != null) {
      variantContextBuilder
          .attribute(INFO_RMMC, List.of(radboudMumcClassification.getId()));
    }
    Classification umcgClassification = consensusRecord.getUmcgClassification();
    if(umcgClassification != null) {
      variantContextBuilder
          .attribute(INFO_UMCG, List.of(umcgClassification.getId()));
    }
    Classification umcuClassification = consensusRecord.getUmcuClassification();
    if(umcuClassification != null) {
      variantContextBuilder
          .attribute(INFO_UMCU, List.of(umcuClassification.getId()));
    }
    Classification vumcClassification = consensusRecord.getVumcClassification();
    if(vumcClassification != null) {
      variantContextBuilder
          .attribute(INFO_VUMC, List.of(vumcClassification.getId()));
    }
    return variantContextBuilder.make();
  }

  private VCFHeader createVcfHeader() {
    VCFHeader vcfHeader = new VCFHeader();
    vcfHeader.setVCFHeaderVersion(VCFHeaderVersion.VCF4_2);

    int idx = 0;
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("1", 249250621), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("2", 243199373), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("3", 198022430), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("4", 191154276), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("5", 180915260), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("6", 171115067), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("7", 159138663), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("8", 146364022), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("9", 141213431), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("10", 135534747), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("11", 135006516), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("12", 133851895), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("13", 115169878), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("14", 107349540), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("15", 102531392), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("16", 90354753), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("17", 81195210), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("18", 78077248), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("19", 59128983), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("20", 63025520), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("21", 48129895), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("22", 51304566), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("X", 155270560), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("Y", 59373566), idx++));
    vcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("MT", 16569), idx));

    vcfHeader.addMetaDataLine(new VCFHeaderLine(HEADER_MVL_VERSION, appSettings.getVersion()));
    vcfHeader.addMetaDataLine(
        new VCFHeaderLine(HEADER_MVL_ARGS, join(" ", appSettings.getArgs())));

    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_VKGL_CL, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_VKGL_CL_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_VKGL_NR, VCFHeaderLineCount.A, VCFHeaderLineType.Integer, INFO_VKGL_NR_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_AMC, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_AMC_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_EMC, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_EMC_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_LUMC, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_LUMC_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_NKI, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_NKI_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_RMMC, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_RMMC_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_UMCG, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_UMCG_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_UMCU, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_UMCU_DESC));
    vcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_VUMC, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_VUMC_DESC));
    return vcfHeader;
  }

  private LinkedHashMap<String, String> createContigMap(String chrom, int length) {
    LinkedHashMap<String, String> contigMap = new LinkedHashMap<>();
    contigMap.put(CONTIG_ID, chrom);
    contigMap.put(CONTIG_LENGTH, String.valueOf(length));
    contigMap.put(CONTIG_ASSEMBLY, ASSEMBLY);
    return contigMap;
  }

  @Override
  public void close() {
    vcfWriter.close();
  }

}
