package com.lothrazar.library.registry;

import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.data.RelativeShape;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FlibDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FutureLibMod.MODID);

    public static final Supplier<DataComponentType<RelativeShape>> RELATIVE_SHAPE = DATA_COMPONENT_TYPES.register("relative_shape",
            () -> DataComponentType.<RelativeShape>builder()
                    .persistent(RelativeShape.CODEC)
                    .networkSynchronized(RelativeShape.STREAM_CODEC)
                    .build());

    // Fallback for legacy NBT interactions that expect a generic map to store arbitrary tags.
    public static final Supplier<DataComponentType<CompoundTag>> CUSTOM_NBT_BUCKET = DATA_COMPONENT_TYPES.register("custom_nbt_bucket",
            () -> DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .networkSynchronized(net.minecraft.network.codec.ByteBufCodecs.COMPOUND_TAG)
                    .build());
}
