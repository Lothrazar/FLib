package com.lothrazar.library.world;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class FeatureBuilder {

  /**
   * Build and register a simple block feature
   * 
   * @param id
   * @param block
   * @return
   */
  public static Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> simple(String id, Block block) {
    return FeatureUtils.register(id, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)));
  }

  public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> patch(String id, Block block) {
    return FeatureUtils.register(id, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block))));
  }

  public static Holder<ConfiguredFeature<OreConfiguration, ?>> ore(ResourceLocation location, RuleTest rule, Block block, int size) {
    return FeatureUtils.register(location.toString(), Feature.ORE, new OreConfiguration(rule, block.defaultBlockState(), size));
  }
}
