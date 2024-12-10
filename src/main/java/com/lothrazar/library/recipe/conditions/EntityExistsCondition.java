package com.lothrazar.library.recipe.conditions;

import com.lothrazar.library.FutureLibMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Example: "conditions": [ {"type": "flib:entity_exists", "value": "veincreeper:coal_creeper" } ]
 */
public class EntityExistsCondition implements ICondition {

  private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FutureLibMod.MODID, "entity_exists");
  private ResourceLocation entityId;

  public EntityExistsCondition(String resourceLocation) {
    this(ResourceLocation.parse(resourceLocation));
  }

  public EntityExistsCondition(ResourceLocation resourceLocation) {
    this.entityId = resourceLocation;
  }

  @Override
  public String toString() {
    return "entity_exists(\"" + entityId + "\")";
  }

//  @Override
//  public ResourceLocation getID() {
//    return ID;
//  }
  //  "conditions": [
  //  {
  //  "type": "flib:entity_exists",
  //  "value": "veincreeper:copper_creeper"
  //  }
  //  ],

  @Override
  public boolean test(IContext context) {
    return this.entityId != null && BuiltInRegistries.ENTITY_TYPE.containsKey(this.entityId);
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return CODEC;
  }


  private static final MapCodec<EntityExistsCondition> CODEC = RecordCodecBuilder.mapCodec(
    instance -> instance.group(
      Codec.STRING.fieldOf("value").forGetter(me -> me.entityId.toString())
    )
    .apply(instance, EntityExistsCondition::new)
  );


/*

  public static class Serializer implements IConditionSerializer<EntityExistsCondition> {

    public static final Serializer INSTANCE = new Serializer();

    @Override
    public void write(JsonObject json, EntityExistsCondition value) {
      json.addProperty("value", value.entityId.toString());
    }

    @Override
    public EntityExistsCondition read(JsonObject json) {
      String entityId = json.get("value").getAsString();
      return new EntityExistsCondition(entityId);
    }

    @Override
    public ResourceLocation getID() {
      return ID;
    }
  }
  */
}
