package com.lothrazar.library.recipe.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Recipe condition that passes only if the given entity type is registered.
 * Example JSON:
 *   "conditions": [{"type": "flib:entity_exists", "value": "veincreeper:coal_creeper"}]
 */
public class EntityExistsCondition implements ICondition {

  public static final MapCodec<EntityExistsCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
          Identifier.CODEC.fieldOf("value").forGetter(c -> c.entityId)
      ).apply(instance, EntityExistsCondition::new));

  private final Identifier entityId;

  public EntityExistsCondition(Identifier entityId) {
    this.entityId = entityId;
  }

  // used to be ICondition.Context
  @Override
  public boolean test(IContext context) {
    return entityId != null && BuiltInRegistries.ENTITY_TYPE.containsKey(entityId);
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return CODEC;
  }

  @Override
  public String toString() {
    return "entity_exists(\"" + entityId + "\")";
  }
}
