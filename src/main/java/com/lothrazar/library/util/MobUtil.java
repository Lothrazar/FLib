package com.lothrazar.library.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class MobUtil {

  public static void removeAttackTargets(LivingEntity attacker) {
    attacker.setLastHurtByMob(null);
    attacker.setLastHurtMob(null);
    if (attacker instanceof Mob mob) {
      mob.setTarget(null);
    }
  }

  public static void disablePickupLoot(Mob mob) {
    if (mob.canPickUpLoot()) {
      mob.setCanPickUpLoot(false);
    }
  }
  //
  //  public static void syncArmFlags(ArmorStand stand, boolean showArms) {
  //    stand.setShowArms(showArms);
  //    stand.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, setBit(stand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS), 4, showArms));
  //  }
  //
  //  private static byte setBit(byte p_31570_, int p_31571_, boolean p_31572_) {
  //    if (p_31572_) {
  //      p_31570_ = (byte) (p_31570_ | p_31571_);
  //    }
  //    else {
  //      p_31570_ = (byte) (p_31570_ & ~p_31571_);
  //    }
  //    return p_31570_;
  //  }
}
