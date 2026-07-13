package com.lothrazar.library.portal;

import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Handles cross-dimension teleportation via the vanilla TeleportTransition API
 * (renamed from DimensionTransition in 26.1). ITeleporter (Forge) no longer exists —
 * entity.changeDimension(TeleportTransition) is the replacement.
 *
 * @see com/lothrazar/cyclic/world/
 */
public class DimensionTransitionWrapper {

  protected ServerLevel world;
  private BlockPosDim target;

  public DimensionTransitionWrapper(ServerLevel world, BlockPosDim target) {
    this.world = world;
    this.target = target;
  }

  private BlockPos moveToSafeCoords(ServerLevel destWorld, BlockPos pos) {
    int tries = 10;
    while (tries > 0) {
      tries--;
      if (destWorld.getBlockState(pos).isSolid()) {
        pos = pos.above();
      }
    }
    return pos;
  }

  /**
   * Builds the TeleportTransition for use with entity.changeDimension().
   * The PostTeleportTransition callback applies effects on the entity after arrival.
   */
  public TeleportTransition buildTransition(Player player) {
    ServerLevel targetLevel = getTargetLevel();
    BlockPos safePos = moveToSafeCoords(targetLevel, target.getPos());
    return new TeleportTransition(
        targetLevel,
        new Vec3(safePos.getX() + 0.5, safePos.getY() + 0.5, safePos.getZ() + 0.5),
        Vec3.ZERO,
        player.getYRot(),
        player.getXRot(),
        entity -> {
          // PostTeleportTransition: runs on the entity after it arrives in the new dimension
          if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 200, 200, false, false));
            living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 20, false, false));
          }
          entity.fallDistance = 0;
        }
    );
  }

  /**
   * Applies pre-teleport effects and plays the portal sound at the destination.
   * Call this before changeDimension().
   */
  public void applyPreTeleportEffects(Player player) {
    if (!player.isCreative() && !player.level().isClientSide()) {
      player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 200, 200, false, false));
    }
    if (this.world != null) {
      this.world.playSound(null,
          target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D,
          SoundEvents.PORTAL_TRAVEL, SoundSource.MASTER,
          0.25F, this.world.getRandom().nextFloat() * 0.4F + 0.8F);
    }
  }

  public ServerLevel getTargetLevel() {
    return world == null ? null : world.getServer().getLevel(LevelWorldUtil.stringToDimension(target.getDimension()));
  }
}
