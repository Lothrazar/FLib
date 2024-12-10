package com.lothrazar.library.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public abstract class ConfigTemplate {

  private static boolean validateItemName(final Object obj)
  {
    return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
  }

//  public CommentedFileConfig setup(final String modid, ModConfigEvent event) {
//    final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(modid + ".toml"))
//        .sync()
//        .autosave()
//        .writingMode(WritingMode.REPLACE)
//        .build();
//    configData.load();
//    return configData;
//  }

  public static ModConfigSpec.Builder builder() {
    return new ModConfigSpec.Builder();
  }
}
