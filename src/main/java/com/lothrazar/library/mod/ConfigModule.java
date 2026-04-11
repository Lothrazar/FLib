package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class ConfigModule {

  private static final ModConfigSpec CONFIG;
  public static BooleanValue ENABLE_COMMANDS;

  static {
    final Builder builder = new ModConfigSpec.Builder();
    builder.comment("General settings").push(FutureLibMod.MODID);
    ENABLE_COMMANDS = builder.comment("If true, the /flib command will be registered").define("command.enabled", true);
    builder.pop();
    CONFIG = builder.build();
  }

  public ConfigModule(ModContainer modContainer) {
    modContainer.registerConfig(ModConfig.Type.COMMON, CONFIG);
  }
}
