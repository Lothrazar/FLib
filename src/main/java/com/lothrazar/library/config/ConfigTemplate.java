package com.lothrazar.library.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public abstract class ConfigTemplate {

  public static Builder builder() {
    return new ModConfigSpec.Builder();
  }
}
