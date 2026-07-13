package com.lothrazar.library.cap.player;

import com.lothrazar.library.FutureLibMod;
import java.util.function.Supplier;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class PlayerCapProvider {

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, FutureLibMod.MODID);

  /**
   * Per-player mana storage, persists across death and dimension changes via AttachmentType serialization.
   */
  public static final Supplier<AttachmentType<PlayerCapabilityStorage>> PLAYER_MANA =
      ATTACHMENT_TYPES.register("player_mana", () ->
          AttachmentType.builder(() -> new PlayerCapabilityStorage())
              .serialize(PlayerCapabilityStorage.CODEC)
              .build());

  /**
   * EntityCapability key for accessing player mana.
   * Usage: player.getCapability(PlayerCapProvider.PLAYER_MANA_CAP)
   * Returns null for non-player entities.
   */
  public static final EntityCapability<PlayerCapabilityStorage, Void> PLAYER_MANA_CAP =
      EntityCapability.createVoid(
          Identifier.fromNamespaceAndPath(FutureLibMod.MODID, "player_mana"),
          PlayerCapabilityStorage.class);
}
