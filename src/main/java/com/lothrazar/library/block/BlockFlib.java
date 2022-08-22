package com.lothrazar.library.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockFlib extends Block {

  public static final EnumProperty<DyeColor> COLOUR = EnumProperty.create("colour", DyeColor.class);
  public static final BooleanProperty LIT = BooleanProperty.create("lit");

  public static class Settings {

    boolean tooltip = false;
    //boolean hasGui = false; // TOOD: BlockCyclic up in here
    //boolean hasFluidInteract = false;

    public Settings tooltip() {
      this.tooltip = true;
      return this;
    }

    public Settings noTooltip() {
      this.tooltip = false;
      return this;
    }

    public void tooltipApply(Block block, List<Component> tooltipList) {
      tooltipList.add(new TranslatableComponent(block.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }

  Settings me;

  public BlockFlib(Properties prop, Settings custom) {
    super(prop);
    this.me = custom;
  }

  public BlockFlib(Properties prop) {
    this(prop, new Settings());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      me.tooltipApply(this, tooltip);
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }
}
