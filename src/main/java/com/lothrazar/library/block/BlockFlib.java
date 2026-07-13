package com.lothrazar.library.block;

import java.util.List;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class BlockFlib extends Block {

  private static final int MAX_CONNECTED_UPDATE = 18;
  public static final EnumProperty<DyeColor> COLOUR = EnumProperty.create("color", DyeColor.class);
  public static final BooleanProperty LIT = BooleanProperty.create("lit");
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public static class Settings {

    boolean tooltip = false;
    boolean rotateColour = false;
    boolean rotateColourConsume = false;
    boolean litWhenPowered;
    private boolean facingAttachment;

    public Settings rotateColour(boolean consume) {
      this.rotateColour = true;
      this.rotateColourConsume = consume;
      return this;
    }

    public Settings litWhenPowered() {
      this.litWhenPowered = true;
      return this;
    }

    public Settings facingAttachment() {
      this.facingAttachment = true;
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
      tooltipList.add(ChatUtil.ilang(block.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
  }

  Settings me;

  public BlockFlib(Properties prop) {
    this(prop, new Settings());
  }

  public BlockFlib(Properties prop, Settings custom) {
    super(prop);
    this.me = custom;
    BlockState def = defaultBlockState();
    if (me.rotateColour) {
      def = def.setValue(COLOUR, DyeColor.WHITE);
    }
    if (me.litWhenPowered) {
      def = def.setValue(LIT, Boolean.valueOf(false));
    }
    this.registerDefaultState(def);
  }

  public static Boolean never(BlockState s, BlockGetter w, BlockPos pos, EntityType<?> t) {
    return (boolean) false;
  }

  public static boolean never(BlockState s, BlockGetter w, BlockPos pos) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canSurvive(BlockState bs, LevelReader level, BlockPos pos) {
    if (me.facingAttachment) {
      Direction dir = bs.getValue(BlockStateProperties.FACING);
      return Block.canSupportCenter(level, pos.relative(dir), dir.getOpposite());
      //          : FaceAttachedHorizontalDirectionalBlock.canAttach(level, pos, dir);
    }
    return super.canSurvive(bs, level, pos);
  }

  @SuppressWarnings("deprecation")
  @Override
  protected BlockState updateShape(BlockState bs, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour,
      BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
    if (me.facingAttachment) {
      return !bs.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState()
          : super.updateShape(bs, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }
    return super.updateShape(bs, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    if (me.litWhenPowered) {
      return this.defaultBlockState().setValue(LIT, Boolean.valueOf(ctx.getLevel().hasNeighborSignal(ctx.getClickedPos())));
    }
    return this.defaultBlockState();
  }

  @Override
  protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, @Nullable Orientation orientation, boolean flagIn) {
    if (me.litWhenPowered && !level.isClientSide()) {
      boolean flag = state.getValue(LIT);
      if (flag != level.hasNeighborSignal(pos)) {
        if (flag) {
          level.scheduleTick(pos, this, 4);
        }
        else {
          level.setBlock(pos, state.cycle(LIT), 2);
        }
      }
    }
  }

  @Override
  public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
    if (me.litWhenPowered && state.getValue(LIT) && !world.hasNeighborSignal(pos)) {
      world.setBlock(pos, state.cycle(LIT), 2);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    //      if (me.poweredByState) {
    //        return blockState.getValue(POWERED) ? 15 : 0;
    //      }
    //TODO: redstone stuff later
    return super.getDirectSignal(blockState, blockAccess, pos, side);
  }

  public void onRightClickBlock(RightClickBlock event, BlockState state) {
    DyeColor newColor = event.getItemStack().get(DataComponents.DYE);
    if (me.rotateColour &&
        event.getItemStack().getItem() instanceof DyeItem && newColor != null) {
      boolean doConnected = event.getEntity().isCrouching();
      rotateDye(state, event.getLevel(), event.getPos(), event.getEntity(), event.getItemStack(), newColor, doConnected);
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

  // TODO 26.1 port: Block#appendHoverText (used in 1.21.1 by BlockItem to add block-specific
  // tooltip lines) was removed entirely - Item's tooltip hook now uses TooltipDisplay/Consumer<Component>
  // instead of List<Component>, and Block no longer has a matching hook at all. This is no longer called
  // automatically by the engine; needs re-wiring (e.g. from BlockItemFlib) if this tooltip text should
  // still show up on the block's item form.
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
    if (me.tooltip) {
      me.tooltipApply(this, tooltip);
    }
  }
}
