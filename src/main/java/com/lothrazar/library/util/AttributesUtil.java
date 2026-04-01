package com.lothrazar.library.util;

import java.util.Collection;
import java.util.Random;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import static com.lothrazar.library.FutureLibMod.MODID;

public class AttributesUtil {

  static final Random RAND = new Random();
  // TODO: we should take id as input? instead of just hardcoding?
  public static final ResourceLocation DEFAULT_ID =  ResourceLocation.fromNamespaceAndPath(MODID, "default");
  public static final ResourceLocation MULT_ID =  ResourceLocation.fromNamespaceAndPath(MODID, "multi");
  public static final ResourceLocation ID_STEP_HEIGHT = ResourceLocation.fromNamespaceAndPath(MODID, "step_height");
  static final float VANILLA = 0.6F;

  //    player.maxUpStep = 0.6F; // LivingEntity.class constructor defaults to this
  public static void disableStepHeight(Player player) {
    AttributeInstance attr = player.getAttribute(Attributes.STEP_HEIGHT);
    attr.removeModifier(ID_STEP_HEIGHT);
  }

  public static void enableStepHeight(Player player) {
    float newVal;
    if (player.isCrouching()) {
      //make sure that, when sneaking, dont fall off!!
      newVal = 0.9F - VANILLA;
    }
    else {
      newVal = 1.0F + (1F / 16F) - VANILLA; //PATH BLOCKS etc are 1/16th downif MY feature turns this on, then do it
    }
    //    player.maxUpStep = newVal; // Deprecated
    AttributeInstance attr = player.getAttribute(Attributes.STEP_HEIGHT);
    AttributeModifier oldModifier = attr.getModifier(AttributesUtil.ID_STEP_HEIGHT);
    double old = oldModifier == null ? 0 : oldModifier.amount();
    if (newVal != old) {
      AttributesUtil.setStepHeightInternal(player, newVal);
    }
  }
  private static void setStepHeightInternal(Player player, double newVal) {
    //    player.maxUpStep = 0.6F; // LivingEntity.class constructor defaults to this
    AttributeInstance attr = player.getAttribute(Attributes.STEP_HEIGHT);
    attr.removeModifier(ID_STEP_HEIGHT);
    if (newVal != 0) {
      AttributeModifier healthModifier = new AttributeModifier(ID_STEP_HEIGHT, newVal, AttributeModifier.Operation.ADD_VALUE);
      attr.addPermanentModifier(healthModifier);
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
      attr.removeModifier(DEFAULT_ID);
      attr.removeModifier(MULT_ID);
    }
    return 0;
  }

  // this is block reach not entity reach
  public static void removePlayerReach(ResourceLocation id, Player player) {

    AttributeInstance attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
    attr.removeModifier(id);
  }

    // this is block reach not entity reach
  public static void setPlayerReach(ResourceLocation id, Player player, int reachBoost) {
    removePlayerReach(id, player);
    AttributeInstance attr = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
    //vanilla is 5, so +11 it becomes 16
    AttributeModifier enchantment = new AttributeModifier(id,  reachBoost, AttributeModifier.Operation.ADD_VALUE);
    attr.addPermanentModifier(enchantment);
  }

  public static void updateAttrModifierBy(Holder<Attribute> attr, ResourceLocation id, Player playerIn, int value) {
    AttributeInstance healthAttribute = playerIn.getAttribute(attr);
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(id);
    //what is our value
    double old = oldHealthModifier == null ? 0 : oldHealthModifier.amount();
    double newVal = value + old;
    healthAttribute.removeModifier(id);
    AttributeModifier healthModifier = new AttributeModifier(id,  newVal, AttributeModifier.Operation.ADD_VALUE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (attr == Attributes.MAX_HEALTH
        && playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }

  public static void multiplyAttrModifierBy(Holder<Attribute> attr, Player playerIn, double value) {
    AttributeInstance healthAttribute = playerIn.getAttribute(attr);
    //what is our value 
    healthAttribute.removeModifier(MULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(MULT_ID, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (attr == Attributes.MAX_HEALTH
        && playerIn.getHealth() > healthAttribute.getValue()) {
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
    healthAttribute.removeModifier(DEFAULT_ID);
    //just remove and replace the modifier
    AttributeModifier healthModifier = new AttributeModifier(DEFAULT_ID,  (modifiedHearts * 2), AttributeModifier.Operation.ADD_VALUE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }
}
