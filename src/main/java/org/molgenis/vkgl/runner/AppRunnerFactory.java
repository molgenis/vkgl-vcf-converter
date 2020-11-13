package org.molgenis.vkgl.runner;

import org.molgenis.vkgl.Settings;

public interface AppRunnerFactory {

  AppRunner create(Settings settings);
}
