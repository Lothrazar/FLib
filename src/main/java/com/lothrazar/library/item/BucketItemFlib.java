package com.lothrazar.library.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

public class BucketItemFlib extends BucketItem {

  public BucketItemFlib(Fluid fluid, Item.Properties props) {
    super(fluid, props
        .craftRemainder(Items.BUCKET)
        .stacksTo(1));
  }

  public BucketItemFlib(Fluid fluid) {
    this(fluid, new Item.Properties());
  }
}
