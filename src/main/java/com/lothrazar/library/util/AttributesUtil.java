package com.lothrazar.library.util;

import java.util.Collection;
import java.util.Random;
import com.lothrazar.library.FutureLibMod;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForgeMod;

public class AttributesUtil {

  static final Random RAND = new Random();
  public static final ResourceLocation DEFAULT_ID = ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "default_id");
  public static final ResourceLocation MULT_ID = ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "mult_id");
  public static final ResourceLocation ID_STEP_HEIGHT = ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "step_height");
  static final float VANILLA = 0.6F;

  public static void disableStepHeight(Player player) {
    AttributeInstance attr = player.getAttribute(Attributes.STEP_HEIGHT);
    if(attr != null) attr.removeModifier(ID_STEP_HEIGHT);
  }

  public static void enableStepHeight(Player player) {
    float newVal;
    if (player.isCrouching()) {
      newVal = 0.9F - VANILLA;
    }
    else {
      newVal = 1.0F + (1F / 16F) - VANILLA;
    }
    AttributeInstance attr = player.getAttribute(Attributes.STEP_HEIGHT);
    if(attr == null) return;

    AttributeModifier oldModifier = attr.getModifier(AttributesUtil.ID_STEP_HEIGHT);
    double old = oldModifier == null ? 0 : oldModifier.amount();
    if (newVal != old) {
      AttributesUtil.setStepHeightInternal(player, newVal);
    }
  }

  private static void setStepHeightInternal(Player player, double newVal) {
    AttributeInstance attr = player.getAttribute(Attributes.STEP_HEIGHT);
    if(attr != null) {
      attr.removeModifier(ID_STEP_HEIGHT);
      if (newVal != 0) {
        AttributeModifier healthModifier = new AttributeModifier(ID_STEP_HEIGHT, newVal, AttributeModifier.Operation.ADD_VALUE);
        attr.addPermanentModifier(healthModifier);
      }
    }
  }

  public static int add(Holder<Attribute> attribute, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, integer);
    }
    return 0;
  }

  public static int addRandom(Holder<Attribute> attribute, Collection<ServerPlayer> players, int min, int max) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, RAND.nextInt(min, max));
    }
    return 0;
  }

  public static int multiply(Holder<Attribute> attribute, Collection<ServerPlayer> players, double integer) {
    for (ServerPlayer playerIn : players) {
      multiplyAttrModifierBy(attribute, playerIn, integer);
    }
    return 0;
  }

  public static int reset(Holder<Attribute> attribute, Collection<ServerPlayer> players) {
    for (ServerPlayer playerIn : players) {
      AttributeInstance attr = playerIn.getAttribute(attribute);
      if(attr != null) {
        attr.removeModifier(DEFAULT_ID);
        attr.removeModifier(MULT_ID);
      }
    }
    return 0;
  }

  public static void removePlayerReach(ResourceLocation id, Player player) {
    AttributeInstance attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
    if(attr != null) attr.removeModifier(id);
  }

  public static void setPlayerReach(ResourceLocation id, Player player, int reachBoost) {
    removePlayerReach(id, player);
    AttributeInstance attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
    if(attr != null) {
      AttributeModifier enchantment = new AttributeModifier(id, reachBoost, AttributeModifier.Operation.ADD_VALUE);
      attr.addPermanentModifier(enchantment);
    }
  }

  public static void updateAttrModifierBy(Holder<Attribute> attr, ResourceLocation id, Player playerIn, int value) {
    AttributeInstance healthAttribute = playerIn.getAttribute(attr);
    if(healthAttribute == null) return;
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(id);
    double old = oldHealthModifier == null ? 0 : oldHealthModifier.amount();
    double newVal = value + old;
    healthAttribute.removeModifier(id);
    AttributeModifier healthModifier = new AttributeModifier(id, newVal, AttributeModifier.Operation.ADD_VALUE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (attr == Attributes.MAX_HEALTH && playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }

  public static void multiplyAttrModifierBy(Holder<Attribute> attr, Player playerIn, double value) {
    AttributeInstance healthAttribute = playerIn.getAttribute(attr);
    if(healthAttribute == null) return;
    healthAttribute.removeModifier(MULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(MULT_ID, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (attr == Attributes.MAX_HEALTH && playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }

  public static int setHearts(Collection<ServerPlayer> players, int finalHearts) {
    for (ServerPlayer playerIn : players) {
      setHearts(finalHearts, playerIn);
    }
    return 0;
  }

  private static void setHearts(int finalHearts, ServerPlayer playerIn) {
    int modifiedHearts = finalHearts - 10;
    AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    if(healthAttribute == null) return;
    healthAttribute.removeModifier(DEFAULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(DEFAULT_ID, (modifiedHearts * 2), AttributeModifier.Operation.ADD_VALUE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }
}
