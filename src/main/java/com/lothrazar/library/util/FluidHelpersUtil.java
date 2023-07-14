package com.lothrazar.library.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class FluidHelpersUtil {

  /**
   * true if the tag and fluid match
   */
  @SuppressWarnings("deprecation")
  public static boolean matches(Fluid fluid, TagKey<Fluid> ft) {
    return (ft != null && fluid != null && fluid.is(ft));
  }
}
