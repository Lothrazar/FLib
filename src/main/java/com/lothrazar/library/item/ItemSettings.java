package com.lothrazar.library.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSettings extends Item {

  public static class CustomItemProperties {

    public boolean tooltip = true;
    public boolean hasEnergy = false; // TODO: itembasecyclic
  }

  private CustomItemProperties custom;

  public ItemSettings(Properties prop, CustomItemProperties custom) {
    super(prop);
    this.custom = custom;
  }

  public ItemSettings(Properties prop) {
    this(prop, new CustomItemProperties());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (custom.tooltip) {
      tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }
}
