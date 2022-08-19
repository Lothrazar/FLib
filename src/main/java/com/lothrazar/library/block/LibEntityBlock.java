package com.lothrazar.library.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class LibEntityBlock extends BaseEntityBlock {

  private LibBlock.Settings me;

  public LibEntityBlock(Properties prop, LibBlock.Settings custom) {
    super(prop);
    this.me = custom;
  }

  public LibEntityBlock(Properties prop) {
    this(prop, new LibBlock.Settings());
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
