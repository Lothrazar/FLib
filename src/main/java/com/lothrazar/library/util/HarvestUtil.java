package com.lothrazar.library.util;

import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class HarvestUtil {

  public static IntegerProperty getAgeProp(BlockState blockState) {
    if (blockState.getBlock() instanceof CropBlock) {
      CropBlock crops = (CropBlock) blockState.getBlock();
      //better mod compatibility if they dont use 'age'
      return crops.getAgeProperty();
    }
    String age = CropBlock.AGE.getName();
    //    ResourceLocation bid = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());
    for (Property<?> p : blockState.getProperties()) {
      if (p != null && p.getName() != null
          && p.getName().equalsIgnoreCase(age)
          && p instanceof IntegerProperty ip) {
        return ip;
      }
    }
    //IGrowable is useless here, i tried. no way to tell if its fully grown, or what age/stage its in
    return null;
  }

  //different plants have different age ranges, such as 3 for cocoa, or 7 for wheat, like BlockStateProperties.AGE_0_5;
  public static boolean hasAgeProperty(BlockState bs) {
    return getAgeProp(bs) != null;
  }
}
