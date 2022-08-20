package com.lothrazar.library.block;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityBlockFlib extends BaseEntityBlock {

  private BlockFlib.Settings me;

  public EntityBlockFlib(Properties prop, BlockFlib.Settings custom) {
    super(prop);
    this.me = custom;
  }

  public EntityBlockFlib(Properties prop) {
    this(prop, new BlockFlib.Settings());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      me.tooltipApply(this, tooltip);
    }
  }
}
