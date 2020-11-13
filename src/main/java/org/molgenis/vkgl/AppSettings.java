package org.molgenis.vkgl;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@NonFinal
public class AppSettings {
  String name;
  String version;
  List<String> args;
}
