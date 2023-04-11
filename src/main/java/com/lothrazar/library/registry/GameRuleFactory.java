package com.lothrazar.library.registry;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * accesstransformers.cfg:
 * 
 * 
 * public net.minecraft.world.level.GameRules$BooleanValue m_46250_(Z)Lnet/minecraft/world/level/GameRules$Type # create(boolean)
 * 
 * @author lothr
 *
 */
public class GameRuleFactory {

  private static final Logger LOGGER = LogManager.getLogger();

  public static Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
    try {
      Type<BooleanValue> ruleTypeBoolean = GameRules.BooleanValue.create(true); // this line fails if the method is private
      Key<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
      LOGGER.info("New gamerule created", id);
      return rule;
    }
    catch (Exception e) {
      return internalCreateBoolean(id, defaultVal, cat);
    }
  }

  @Deprecated
  @SuppressWarnings("unchecked")
  public static Key<BooleanValue> internalCreateBoolean(String id, boolean defaultVal, Category cat) {
    try {
      Method m = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
      m.setAccessible(true);
      Type<BooleanValue> ruleTypeBoolean = (Type<BooleanValue>) m.invoke(null, defaultVal);
      Key<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
      LOGGER.info("new gamerule created", id);
      return rule;
    }
    catch (Exception e) {
      LOGGER.error("Create gamerule error", e);
    }
    return null;
  }
}
