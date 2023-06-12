package com.lothrazar.library.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AutoSprintUtil {

  private static final String NBT = "isautorunning";

  public static Vec3 vector(Player p, float speed) {
    p.zza = speed;
    Vec3 vec = new Vec3(p.xxa, p.yya, p.zza);
    return vec;
  }

  public static void moveAlongVector(Boat p, Vec3 vec) {
    Level world = p.level();
    BlockPos blockpos = BlockPos.containing(p.getX(), p.getBoundingBox().minY - 1.0D, p.getZ());
    BlockState blockState = world.getBlockState(blockpos);
    float f5 = blockState.getFriction(world, blockpos, p);
    p.moveRelative(getRelevantMoveFactorBoat(p, f5), vec);
  }

  public static void moveAlongVector(LivingEntity p, Vec3 vec) {
    Level world = p.level();
    BlockPos blockpos = BlockPos.containing(p.getX(), p.getBoundingBox().minY - 1.0D, p.getZ());
    BlockState bs = world.getBlockState(blockpos);
    float f5 = bs.getFriction(world, blockpos, p);
    p.moveRelative(getRelevantMoveFactor(p, f5), vec);
  }

  public static float getRelevantMoveFactor(LivingEntity p, float flt) {
    if (p instanceof Player) {
      Player pl = (Player) p;
      if (pl.isCreative()) {
        return p.getSpeed() * (0.21600002F / (flt * flt * flt));
      }
    }
    return p.onGround()// isOnGround
        ? p.getSpeed() * (0.21600002F / (flt * flt * flt))
        // getFlyingSpeed is private so i copied formula
        : (p.getSpeed() * 0.1F); // p.getFlyingSpeed();
  }

  public static float getRelevantMoveFactorBoat(Boat p, float flt) {
    // boat has no getAIMoveSpeed(), so we hardcode it
    final float aiMoveSpeedMock = 0.0383F;
    return aiMoveSpeedMock * (0.21600002F / (flt * flt * flt));
  }

  public static void setAutorunState(Player player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.displayClientMessage(Component.translatable("autorun." + value), true);
    player.setSprinting(value);
  }

  public static boolean getAutorunState(Player player) {
    if (player == null || player.getPersistentData() == null) {
      return false;
    }
    return player.getPersistentData().getBoolean(NBT);
  }

  public static boolean doesKeypressHaltSprint(Player p) {
    if (p.getVehicle() instanceof Boat) {
      // boats can still turn left/right 
      return Minecraft.getInstance().options.keyUp.isDown() ||
          Minecraft.getInstance().options.keyDown.isDown();
    }
    return Minecraft.getInstance().options.keyUp.isDown() ||
        Minecraft.getInstance().options.keyDown.isDown() ||
        Minecraft.getInstance().options.keyLeft.isDown() ||
        Minecraft.getInstance().options.keyRight.isDown();
  }
}
