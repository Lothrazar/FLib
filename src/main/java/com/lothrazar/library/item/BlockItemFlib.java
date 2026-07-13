package com.lothrazar.library.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.library.block.BlockFlib;
import com.lothrazar.library.item.ItemFlib.Settings;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;

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
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flagIn) {
    List<Component> tooltip = new ArrayList<>();
    if (me.tooltip) {
      me.tooltipApply(this, tooltip);
    }
    if (this.getBlock() instanceof BlockFlib blockFlib) {
      blockFlib.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
    tooltip.forEach(tooltipAdder);
    super.appendHoverText(stack, worldIn, display, tooltipAdder, flagIn);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType, FuelValues fuelValues) {
    return me.burnTime;
  }
}
