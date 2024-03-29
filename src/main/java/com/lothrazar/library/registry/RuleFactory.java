package com.lothrazar.library.registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;

public class RuleFactory {

  public static final Logger LOGGER = LogManager.getLogger();

  public static GameRules.Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
    LOGGER.info("Attempting to register a new gamerule : " + id);
    var ruleTypeBoolean = GameRules.BooleanValue.create(defaultVal); // this works if AT works
    return GameRules.register(id, cat, ruleTypeBoolean);
    //accesstransformer started working, it was this before
    //    try {
    //      Method m = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
    //      //      m.setAccessible(true);
    //      Type<BooleanValue> ruleTypeBoolean = (Type<BooleanValue>) m.invoke(null, defaultVal);
    //      Key<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
    //      return rule;
    //    }
    //    catch (Exception e) {
    //      LOGGER.error("Create gamerule error", e);
    //    }
    //    return null;
  }
}
