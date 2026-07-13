package com.lothrazar.library.fluid;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class GenericFluidBlock extends LiquidBlock {

  private final List<Consumer<LivingEntity>> onInside;

  public GenericFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties props) {
    this(supplier, props, List.of());
  }

  public GenericFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties props,
                           List<Consumer<LivingEntity>> onInside) {
    super(supplier.get(), props.noOcclusion());
    this.onInside = onInside;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
    if (!level.isClientSide() && !onInside.isEmpty() && entity instanceof LivingEntity ent) {
      for (Consumer<LivingEntity> effect : onInside) {
        effect.accept(ent);
      }
    }
    super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
  }
}
