package com.lothrazar.library.packet;

import java.util.function.Supplier;
import com.lothrazar.library.FutureLibMod;
import com.lothrazar.library.cap.player.PlayerCapabilityStorage;
import com.lothrazar.library.cap.world.ChunkDataStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Sync Player and Chunk capabilities to client
 *
 */
public class PacketSyncManaToClient extends PacketBaseFlib {

  private int playerMana; // TODO : playerData object
  private int chunkMana; // TODO: chunkData object

  public PacketSyncManaToClient(PlayerCapabilityStorage playerMana, ChunkDataStorage chunkMana) {
    this.playerMana = playerMana.getMana();
    this.chunkMana = chunkMana.getMana();
  }
  //  public PacketSyncManaToClient() {}

  public static void handle(PacketSyncManaToClient message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      FutureLibMod.LOGGER.info("client sync message spam " + message);
      //      ClientDataManager.set(message.playerMana, message.chunkMana);
    });
    message.done(ctx);
  }

  public static PacketSyncManaToClient decode(FriendlyByteBuf buf) {
    return new PacketSyncManaToClient(new PlayerCapabilityStorage(buf.readInt()), new ChunkDataStorage(buf.readInt()));
  }

  public static void encode(PacketSyncManaToClient msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.playerMana);
    buf.writeInt(msg.chunkMana);
  }
}
