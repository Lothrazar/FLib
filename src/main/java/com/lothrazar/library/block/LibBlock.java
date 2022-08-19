package com.lothrazar.library.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LibBlock extends Block {

  public static class Settings {

    boolean tooltip = true;
    //boolean hasGui = false; // TOOD: BlockCyclic up in here
    //boolean hasFluidInteract = false;
    RenderShape rendershape = RenderShape.MODEL; // try INVISIBLE for some tile entities

    public Settings tooltip() {
      this.tooltip = true;
      return this;
    }

    public Settings noTooltip() {
      this.tooltip = false;
      return this;
    }
  }

  private Settings me;

  public LibBlock(Properties prop, Settings custom) {
    super(prop);
    this.me = custom;
  }

  public LibBlock(Properties prop) {
    this(prop, new Settings());
  }

  @Override
  public RenderShape getRenderShape(BlockState bs) {
    return me.rendershape;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }
}
