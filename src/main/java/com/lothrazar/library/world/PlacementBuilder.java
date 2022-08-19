package com.lothrazar.library.world;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;

public class PlacementBuilder {

  /**
   * Places small features on sturdy surfaces with air around. so like flowers on stone, does not replace blocks like ore veins do
   * 
   * @param id
   * @param feature
   * @param count
   * @param bottom
   * @param top
   * @return
   */
  public static Holder<PlacedFeature> smallSturdy(String id, Holder<? extends ConfiguredFeature<?, ?>> feature, Integer count, Integer bottom, Integer top) {
    return PlacementUtils.register(id, feature,
        CountPlacement.of(count),
        InSquarePlacement.spread(),
        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(bottom), VerticalAnchor.belowTop(top)),
        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
        RandomOffsetPlacement.vertical(ConstantInt.of(1)),
        BiomeFilter.biome());
  }
}
