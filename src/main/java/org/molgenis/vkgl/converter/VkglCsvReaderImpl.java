package org.molgenis.vkgl.converter;

import static java.util.Objects.requireNonNull;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;
import org.molgenis.vkgl.converter.model.ConsensusRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VkglCsvReaderImpl implements VkglCsvReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(VkglCsvReaderImpl.class);

  private final Reader reader;

  public VkglCsvReaderImpl(Reader reader) {
    this.reader = requireNonNull(reader);
  }

  @Override
  public List<ConsensusRecord> read() {
    CsvToBean<ConsensusRecord> csvToBean =
        new CsvToBeanBuilder<ConsensusRecord>(reader)
            .withSeparator(',')
            .withType(ConsensusRecord.class)
            .withThrowExceptions(false)
            .build();
    List<ConsensusRecord> consensusRecords = csvToBean.parse();
    List<CsvException> csvExceptions = csvToBean.getCapturedExceptions();

    csvExceptions.forEach(
        csvException ->
            LOGGER.error(
                "skipping consensus record due to error '{}:{}': line #{} {}",
                csvException.getLine()[2],
                csvException.getLine()[3],
                csvException.getLineNumber(),
                csvException.getMessage()));
    return consensusRecords;
  }

  @Override
  public void close() {
    try {
      reader.close();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
