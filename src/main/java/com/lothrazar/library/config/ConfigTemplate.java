package com.lothrazar.library.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.loading.FMLPaths;

public abstract class ConfigTemplate {

  public CommentedFileConfig setup(final String modid) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(modid + ".toml"))
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    return configData;
  }

  public static Builder builder() {
    return new ForgeConfigSpec.Builder();
  }
}
