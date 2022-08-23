package com.lothrazar.library.block;

import java.util.List;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class BlockFlib extends Block {

  private static final int MAX_CONNECTED_UPDATE = 18;
  public static final EnumProperty<DyeColor> COLOUR = EnumProperty.create("colour", DyeColor.class);
  public static final BooleanProperty LIT = BooleanProperty.create("lit");

  public static class Settings {

    boolean tooltip = false;
    boolean rotateColour = false;
    boolean rotateColourConsume = false;
    //boolean hasGui = false; // TOOD: BlockCyclic up in here
    //boolean hasFluidInteract = false;

    public Settings rotateColour(boolean consume) {
      this.rotateColour = true;
      this.rotateColourConsume = consume;
      return this;
    }

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
  //  @Override
  //  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
  //    ItemStack heldStack = player.getItemInHand(hand);
  //    if (me.rotateColour && state.hasProperty(COLOUR) && heldStack.getItem() instanceof DyeItem dye) {
  //      rotateDye(state, world, pos, player, heldStack, dye.getDyeColor(), false);
  //      return InteractionResult.SUCCESS;
  //    }
  //    return super.use(state, world, pos, player, hand, hit);
  //  }

  public void onRightClickBlock(RightClickBlock event, BlockState state) {
    if (me.rotateColour &&
        event.getItemStack().getItem() instanceof DyeItem newColor) {
      boolean doConnected = event.getPlayer().isCrouching();
      rotateDye(state, event.getWorld(), event.getPos(), event.getPlayer(), event.getItemStack(), newColor.getDyeColor(), doConnected);
    }
  }

  public void rotateDye(BlockState state, Level world, BlockPos pos, Player player, ItemStack heldStack, DyeColor newColour, boolean doConnected) {
    DyeColor oldColour = state.getValue(COLOUR);
    if (newColour != oldColour) {
      //new color is different, NOW update
      world.setBlockAndUpdate(pos, state.setValue(COLOUR, newColour));
      if (me.rotateColourConsume) {
        ItemStackUtil.shrink(player, heldStack);
      }
      if (doConnected) {
        this.setConnectedColour(world, pos, oldColour, newColour, 0);
      }
    }
  }

  public void setConnectedColour(Level world, BlockPos pos, DyeColor oldColour, DyeColor newColor, int rec) {
    if (rec > MAX_CONNECTED_UPDATE) {
      return;
    }
    for (Direction d : Direction.values()) {
      BlockPos offset = pos.relative(d);
      BlockState here = world.getBlockState(offset);
      if (here.getBlock() == this && oldColour == here.getValue(COLOUR)) {
        world.setBlockAndUpdate(offset, here.setValue(COLOUR, newColor));
        rec++;
        this.setConnectedColour(world, offset, oldColour, newColor, rec);
      }
    }
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
