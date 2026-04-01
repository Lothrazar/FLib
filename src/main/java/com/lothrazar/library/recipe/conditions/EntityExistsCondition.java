package com.lothrazar.library.recipe.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Recipe condition that passes only if the given entity type is registered.
 * Example JSON:
 *   "conditions": [{"type": "flib:entity_exists", "value": "veincreeper:coal_creeper"}]
 */
public class EntityExistsCondition implements ICondition {

  public static final MapCodec<EntityExistsCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
          ResourceLocation.CODEC.fieldOf("value").forGetter(c -> c.entityId)
      ).apply(instance, EntityExistsCondition::new));

  private final ResourceLocation entityId;

  public EntityExistsCondition(ResourceLocation entityId) {
    this.entityId = entityId;
  }

  @Override
  public boolean test(ICondition.Context context) {
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
