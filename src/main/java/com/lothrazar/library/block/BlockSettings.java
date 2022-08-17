package com.lothrazar.library.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockSettings extends BaseEntityBlock {

  public static class CustomBlockProperties {

    public boolean tooltip = true;
    boolean hasGui = false; // TOOD: BlockCyclic up in here
    boolean hasFluidInteract = false;
    RenderShape rendershape = RenderShape.MODEL; // try INVISIBLE for some tile entities
  }

  private CustomBlockProperties custom;

  public BlockSettings(Properties prop, CustomBlockProperties custom) {
    super(prop);
    this.custom = custom;
  }

  public BlockSettings(Properties prop) {
    this(prop, new CustomBlockProperties());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos p, BlockState st) {
    // if custom.hasEntity == true, throw ?
    return null; // override if you actually have an Entity
  }

  @Override
  public RenderShape getRenderShape(BlockState bs) {
    return custom.rendershape;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (custom.tooltip) {
      tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }
}
