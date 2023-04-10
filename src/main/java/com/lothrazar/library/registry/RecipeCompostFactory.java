package com.lothrazar.library.registry;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

/**
 * Wrapper for ComposterBlock hardcoded map and values
 *
 */
public class RecipeCompostFactory {

  public static final class FillValues {

    /**
     * leaves kelp grass seeds berries
     */
    public static final float LEAVES = 0.3F;
    /**
     * vines, cactus, sugar cane, melon slices
     */
    public static final float VINES = 0.5F;
    /**
     * flowers, mushrooms, moss
     */
    public static final float FLOWER = 0.65F;
    /**
     * Baked potato, bread, mushroom blocks, wart blocks
     */
    public static final float HAY = 0.85F;
    /**
     * cake, pumkin pie
     */
    public static final float FULL = 1F;
  }

  public static void put(ItemLike itemLike, float val) {
    ComposterBlock.COMPOSTABLES.put(itemLike, val);
  }
}
