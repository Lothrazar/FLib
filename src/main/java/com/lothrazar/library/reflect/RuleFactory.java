package com.lothrazar.library.reflect;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class RuleFactory {

  public static final Logger LOGGER = LogManager.getLogger();

  @SuppressWarnings("unchecked")
  public static Key<BooleanValue> createBoolean(String id, boolean defaultVal, Category cat) {
    //access transformers cfg SHULD make this create public
    //   var ruleTypeBoolean2 = GameRules.BooleanValue.create(true); // this works if AT works
    try {
      Method m = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
      //      m.setAccessible(true);
      Type<BooleanValue> ruleTypeBoolean = (Type<BooleanValue>) m.invoke(null, defaultVal);
      Key<BooleanValue> rule = GameRules.register(id, cat, ruleTypeBoolean);
      return rule;
    }
    catch (Exception e) {
      LOGGER.error("Create gamerule error", e);
    }
    return null;
  }
}
