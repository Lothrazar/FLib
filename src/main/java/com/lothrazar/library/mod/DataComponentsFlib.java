package com.lothrazar.library.mod;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.data.RelativeShape;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataComponentsFlib {
  public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FutureLibMod.MODID);

  public static final Supplier<DataComponentType<RelativeShape>> RELATIVE_SHAPE = DATA_COMPONENT_TYPES.register("relative_shape",
      () -> DataComponentType.<RelativeShape>builder()
          .persistent(RelativeShape.CODEC)
          .networkSynchronized(RelativeShape.STREAM_CODEC)
          .build());

}
