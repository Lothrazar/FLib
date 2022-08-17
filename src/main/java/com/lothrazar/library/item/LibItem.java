package com.lothrazar.library.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LibItem extends Item {

  public static class Settings {

    private boolean tooltip = true;
    private boolean hasEnergy = false; // TODO: itembasecyclic
    private int burnTime = 0;

    public Settings tooltip() {
      this.tooltip = true;
      return this;
    }

    public Settings noTooltip() {
      this.tooltip = false;
      return this;
    }

    public void burn(int burnTicks) {
      burnTime = burnTicks;
    }
  }

  private Settings me;

  public LibItem(Properties prop, Settings custom) {
    super(prop);
    this.me = custom;
  }

  public LibItem(Properties prop) {
    this(prop, new Settings());
  }

  @Override
  public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
    return me.burnTime;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }
}
