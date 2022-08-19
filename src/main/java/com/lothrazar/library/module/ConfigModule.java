package com.lothrazar.library.module;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.lothrazar.library.FutureLibMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigModule {

  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static BooleanValue ENABLE_COMMANDS;
  static {
    initConfig();
  }

  private static void initConfig() {
    CFG.comment("General settings").push(FutureLibMod.MODID);
    ENABLE_COMMANDS = CFG.comment("If true, the /flib command will be registered").define("command.enabled", true);
    CFG.pop(); // one pop for every push
    COMMON_CONFIG = CFG.build();
  }

  public static void setup() {
    final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(FutureLibMod.MODID + ".toml"))
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }
}
