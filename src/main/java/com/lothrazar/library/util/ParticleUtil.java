package com.lothrazar.library.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleUtil {

  public static void spawnParticleBeam(Level world, ParticleOptions sparkle, BlockPos start, BlockPos end, int count) {
    // thanks to http://www.minecraftforge.net/forum/index.php?topic=30567.0
    // and http://mathforum.org/library/drmath/view/65721.html
    float dX = end.getX() - start.getX();
    float dY = end.getY() - start.getY();
    float dZ = end.getZ() - start.getZ();
    float t, x, y, z;
    for (t = 0.0F; t < 1.0F; t += 0.09F) {
      x = start.getX() + (dX * t);
      y = start.getY() + (dY * t);
      z = start.getZ() + (dZ * t);
      ParticleUtil.spawnParticle(world, sparkle, x, y, z, count);
    }
  }

  private static final double RANDOM_HORIZ = 0.8;
  private static final double RANDOM_VERT = 1.5;

  private static double getVertRandom(Level world, double rando) {
    return world.random.nextDouble() * rando - 0.1;
  }

  private static double getHorizRandom(Level world, double rando) {
    return (world.random.nextDouble() - 0.5D) * rando;
  }

  public static void spawnParticle(Level world, ParticleOptions sparkle, BlockPos pos, int count) {
    if (world.isClientSide) {
      spawnParticle(world, sparkle, pos.getX() + .5F, pos.getY() + .5F, pos.getZ() + .5F, count);
    }
  }

  /**
   * always check IS CLIENTSIDE before this
   *
   * @param world
   * @param sparkle
   * @param x
   * @param y
   * @param z
   * @param count
   */
  private static void spawnParticle(Level world, ParticleOptions sparkle, float x, float y, float z, int count) {
    for (int countparticles = 0; countparticles <= count; ++countparticles) {
      Minecraft.getInstance().particleEngine.createParticle(sparkle,
          x + getHorizRandom(world, RANDOM_HORIZ),
          y + getVertRandom(world, RANDOM_VERT),
          z + getHorizRandom(world, RANDOM_HORIZ),
          0.0D, 0.0D, 0.0D);
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static void spawnBlockParticles(SimpleParticleType partIn, Level worldIn, BlockPos pos, RandomSource rand) {
    double x = pos.getX() + rand.nextDouble();
    double y = pos.getY() + rand.nextDouble();
    double z = pos.getZ() + rand.nextDouble();
    double xSp = (rand.nextDouble() - 0.5D) * 0.5D;
    double ySp = (rand.nextDouble() - 0.5D) * 0.5D;
    double zSp = (rand.nextDouble() - 0.5D) * 0.5D;
    worldIn.addParticle(partIn, x, y, z, xSp, ySp, zSp);
  }

  public static void doFireworks(Player player, Level world, double z, double y, double x) {
    Entity rocket = new FireworkRocketEntity(world, new ItemStack(Items.FIREWORK_ROCKET), player);
    rocket.setPos(x, y, z);
    world.addFreshEntity(rocket);
  }

  public static void doSmoke(Level world, double z, double y, double x) {
    for (int i = 0; i < 20; i++) {
      world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.2D, 0.0D);
    }
  }
}
