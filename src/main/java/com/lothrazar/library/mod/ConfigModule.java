package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.config.ConfigTemplate;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ConfigModule extends ConfigTemplate {

  private static ForgeConfigSpec CONFIG;
  public static BooleanValue ENABLE_COMMANDS;
  static {
    final Builder builder = builder();
    builder.comment("General settings").push(FutureLibMod.MODID);
    ENABLE_COMMANDS = builder.comment("If true, the /flib command will be registered").define("command.enabled", true);
    builder.pop(); // one pop for every push
    CONFIG = builder.build();
  }

  public ConfigModule() {
    CONFIG.setConfig(setup(FutureLibMod.MODID));
  }
}
