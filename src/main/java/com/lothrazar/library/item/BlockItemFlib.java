package com.lothrazar.library.item;

import java.util.List;
import com.lothrazar.library.item.ItemFlib.Settings;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockItemFlib extends BlockItem {

  private Settings me;

  public BlockItemFlib(Block b, Properties prop, Settings custom) {
    super(b, prop);
    this.me = custom;
  }

  public BlockItemFlib(Block b, Properties prop) {
    this(b, prop, new Settings());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      me.tooltipApply(this, tooltip);
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
    return me.burnTime;
  }
}
