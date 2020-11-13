package org.molgenis.vkgl.converter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFHeader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vkgl.AppSettings;

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
    // FIXME
    throw new RuntimeException();
//    ConsensusVariant consensusVariant0 =
//        ConsensusVariant.builder()
//            .chrom("16")
//            .pos(8800000)
//            .ref("A")
//            .alt("G")
//            .classification(BENIGN)
//            .build();
//    ConsensusVariant consensusVariant1 =
//        ConsensusVariant.builder()
//            .chrom("1")
//            .pos(9000000)
//            .ref("A")
//            .alt("G")
//            .classification(PATHOGENIC)
//            .build();
//    List<ConsensusVariant> consensusVariants = List.of(consensusVariant0, consensusVariant1);
//
//    vkglVcfWriter.write(consensusVariants);
//
//    ArgumentCaptor<VariantContext> variantContextCaptor =
//        ArgumentCaptor.forClass(VariantContext.class);
//    verify(vcfWriter, times(2)).add(variantContextCaptor.capture());
//
//    List<VariantContext> variantContexts = variantContextCaptor.getAllValues();
//    assertAll(
//        () -> {
//          VariantContext variantContext0 = variantContexts.get(0);
//          assertAll(
//              () -> assertEquals("1", variantContext0.getContig()),
//              () -> assertEquals(9000000, variantContext0.getStart()),
//              () -> assertEquals("A", variantContext0.getReference().getBaseString()),
//              () ->
//                  assertEquals(
//                      List.of("G"),
//                      variantContext0.getAlternateAlleles().stream()
//                          .map(Allele::getBaseString)
//                          .collect(toList())),
//              () -> assertEquals(List.of("P"), variantContext0.getAttribute("MVL")));
//        },
//        () -> {
//          VariantContext variantContext1 = variantContexts.get(1);
//          assertAll(
//              () -> assertEquals("16", variantContext1.getContig()),
//              () -> assertEquals(8800000, variantContext1.getStart()),
//              () -> assertEquals("A", variantContext1.getReference().getBaseString()),
//              () ->
//                  assertEquals(
//                      List.of("G"),
//                      variantContext1.getAlternateAlleles().stream()
//                          .map(Allele::getBaseString)
//                          .collect(toList())),
//              () -> assertEquals(List.of("B"), variantContext1.getAttribute("MVL")));
//        });
  }
}
