package com.lothrazar.library.recipe.conditions;

import com.google.gson.JsonObject;
import com.lothrazar.library.FutureLibMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Example: "conditions": [ {"type": "flib:entity_exists", "value": "veincreeper:coal_creeper" } ]
 */
public class EntityExistsCondition implements ICondition {

  private static final ResourceLocation ID = new ResourceLocation(FutureLibMod.MODID, "entity_exists");
  private ResourceLocation entityId;

  public EntityExistsCondition(ResourceLocation resourceLocation) {
    this.entityId = resourceLocation;
  }

  @Override
  public String toString() {
    return "entity_exists(\"" + entityId + "\")";
  }

  @Override
  public ResourceLocation getID() {
    return ID;
  }
  //  "conditions": [
  //  {
  //  "type": "flib:entity_exists",
  //  "value": "veincreeper:copper_creeper"
  //  }
  //  ],

  @Override
  public boolean test(IContext context) {
    return this.entityId != null && ForgeRegistries.ENTITY_TYPES.containsKey(this.entityId);
  }

  public static class Serializer implements IConditionSerializer<EntityExistsCondition> {

    public static final Serializer INSTANCE = new Serializer();

    @Override
    public void write(JsonObject json, EntityExistsCondition value) {
      json.addProperty("value", value.entityId.toString());
    }

    @Override
    public EntityExistsCondition read(JsonObject json) {
      String entityId = json.get("value").getAsString();
      return new EntityExistsCondition(new ResourceLocation(entityId));
    }

    @Override
    public ResourceLocation getID() {
      return ID;
    }
  }
}
