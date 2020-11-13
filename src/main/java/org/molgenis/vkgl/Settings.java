package org.molgenis.vkgl;

import java.net.URI;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Settings {
  Path inputVkglConsensusCsvPath;
  AppSettings appSettings;
  WriterSettings writerSettings;
}
