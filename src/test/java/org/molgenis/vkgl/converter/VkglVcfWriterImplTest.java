package org.molgenis.vkgl.converter;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFHeader;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vkgl.AppSettings;
import org.molgenis.vkgl.converter.model.Classification;
import org.molgenis.vkgl.converter.model.ConsensusClassification;
import org.molgenis.vkgl.converter.model.ConsensusRecord;

@ExtendWith({MockitoExtension.class})
class VkglVcfWriterImplTest {

  @Mock
  VariantContextWriter vcfWriter;
  @Mock
  AppSettings appSettings;
  private VkglVcfWriterImpl vkglVcfWriter;

  @BeforeEach
  void setUp() {
    vkglVcfWriter = new VkglVcfWriterImpl(vcfWriter, appSettings);
  }

  @AfterEach
  void tearDown() {
    vkglVcfWriter.close();
  }

  @Test
  void writeHeader() {
    // see integration test for additional tests
    vkglVcfWriter.writeHeader();
    verify(vcfWriter).writeHeader(any(VCFHeader.class));
  }

  @Test
  void write() {
    ConsensusRecord consensusRecord0 = createConsensusRecord0();
    ConsensusRecord consensusRecord1 = createConsensusRecord1();
    List<ConsensusRecord> consensusRecords = List.of(consensusRecord0, consensusRecord1);

    vkglVcfWriter.write(consensusRecords);

    ArgumentCaptor<VariantContext> variantContextCaptor =
        ArgumentCaptor.forClass(VariantContext.class);
    verify(vcfWriter, times(2)).add(variantContextCaptor.capture());

    List<VariantContext> variantContexts = variantContextCaptor.getAllValues();
    assertAll(
        () -> {
          VariantContext variantContext0 = variantContexts.get(0);
          assertAll(
              () -> assertEquals("1", variantContext0.getContig()),
              () -> assertEquals(123, variantContext0.getStart()),
              () -> assertEquals("C", variantContext0.getReference().getBaseString()),
              () ->
                  assertEquals(
                      List.of("T"),
                      variantContext0.getAlternateAlleles().stream()
                          .map(Allele::getBaseString)
                          .collect(toList())),
              () -> assertEquals(List.of("B"), variantContext0.getAttribute("AMC")),
              () -> assertEquals(List.of("LB"), variantContext0.getAttribute("EMC")),
              () -> assertEquals(List.of("VUS"), variantContext0.getAttribute("LUMC")),
              () -> assertEquals(List.of("LP"), variantContext0.getAttribute("NKI")),
              () -> assertEquals(List.of("P"), variantContext0.getAttribute("RMMC")),
              () -> assertEquals(List.of("B"), variantContext0.getAttribute("UMCG")),
              () -> assertEquals(List.of("LB"), variantContext0.getAttribute("UMCU")),
              () -> assertEquals(List.of("VUS"), variantContext0.getAttribute("VUMC")));
        },
        () -> {
          VariantContext variantContext1 = variantContexts.get(1);
          assertAll(
              () -> assertEquals("2", variantContext1.getContig()),
              () -> assertEquals(234, variantContext1.getStart()),
              () -> assertEquals("A", variantContext1.getReference().getBaseString()),
              () ->
                  assertEquals(
                      List.of("G"),
                      variantContext1.getAlternateAlleles().stream()
                          .map(Allele::getBaseString)
                          .collect(toList())),
              () -> assertEquals(List.of("B"), variantContext1.getAttribute("AMC")),
              () -> assertEquals(List.of("B"), variantContext1.getAttribute("VKGL_CL")),
              () -> assertEquals(List.of(1), variantContext1.getAttribute("VKGL_NR")));
        });
  }

  private ConsensusRecord createConsensusRecord1() {
    return ConsensusRecord.builder().chromosome("2").pos(234).ref("A")
        .alt("G")
        .amcClassification(Classification.BENIGN)
        .consensusClassification(ConsensusClassification.LIKELY_BENIGN).matches(1).build();
  }

  @Test
  void writePublic() {
    vkglVcfWriter.setWritePublic(true);

    ConsensusRecord consensusRecord0 = createConsensusRecord0();
    ConsensusRecord consensusRecord1 = createConsensusRecord1();
    List<ConsensusRecord> consensusRecords = List.of(consensusRecord0, consensusRecord1);

    vkglVcfWriter.write(consensusRecords);

    ArgumentCaptor<VariantContext> variantContextCaptor =
        ArgumentCaptor.forClass(VariantContext.class);
    verify(vcfWriter, times(1)).add(variantContextCaptor.capture());

    List<VariantContext> variantContexts = variantContextCaptor.getAllValues();
    assertAll(
        () -> {
          VariantContext variantContext1 = variantContexts.get(0);
          assertAll(
              () -> assertEquals("2", variantContext1.getContig()),
              () -> assertEquals(234, variantContext1.getStart()),
              () -> assertEquals("A", variantContext1.getReference().getBaseString()),
              () ->
                  assertEquals(
                      List.of("G"),
                      variantContext1.getAlternateAlleles().stream()
                          .map(Allele::getBaseString)
                          .collect(toList())),
              () -> assertEquals(List.of("B"), variantContext1.getAttribute("VKGL_CL")),
              () -> assertEquals(List.of(1), variantContext1.getAttribute("VKGL_NR")));
        });
  }

  private ConsensusRecord createConsensusRecord0() {
    return ConsensusRecord.builder().chromosome("1").pos(123).ref("C")
        .alt("T").amcClassification(
            Classification.BENIGN).erasmusClassification(Classification.LIKELY_BENIGN)
        .lumcClassification(Classification.VUS).nkiClassification(Classification.LIKELY_PATHOGENIC)
        .radboudMumcClassification(Classification.PATHOGENIC)
        .umcgClassification(Classification.BENIGN).umcuClassification(Classification.LIKELY_BENIGN)
        .vumcClassification(Classification.VUS).consensusClassification(
            ConsensusClassification.NO_CONSENSUS).build();
  }

}
