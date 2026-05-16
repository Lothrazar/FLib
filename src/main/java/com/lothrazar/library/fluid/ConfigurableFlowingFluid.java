package com.lothrazar.library.fluid;

import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

/***
 * Helper for flowing and source blocks that use slope and dropoff params
 */
public final class ConfigurableFlowingFluid {

  private ConfigurableFlowingFluid() {}

  public static class Flowing extends BaseFlowingFluid.Flowing {
    private final int slopeFindDistance, dropOff;
    public Flowing(Properties props, int slopeFindDistance, int dropOff) {
      super(props);
      this.slopeFindDistance = slopeFindDistance;
      this.dropOff = dropOff;
    }
    @Override public int getSlopeFindDistance(LevelReader w) { return slopeFindDistance; }
    @Override public int getDropOff(LevelReader w) { return dropOff; }
  }

  public static class Source extends BaseFlowingFluid.Source {
    private final int slopeFindDistance, dropOff;
    public Source(Properties props, int slopeFindDistance, int dropOff) {
      super(props);
      this.slopeFindDistance = slopeFindDistance;
      this.dropOff = dropOff;
    }
    @Override public int getSlopeFindDistance(LevelReader w) { return slopeFindDistance; }
    @Override public int getDropOff(LevelReader w) { return dropOff; }
  }
}
