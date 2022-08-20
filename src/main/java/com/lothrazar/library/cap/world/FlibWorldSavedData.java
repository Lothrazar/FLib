package com.lothrazar.library.cap.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import com.lothrazar.library.cap.player.PlayerCapProvider;
import com.lothrazar.library.cap.player.PlayerCapabilityStorage;
import com.lothrazar.library.module.PacketRegistry;
import com.lothrazar.library.packet.PacketSyncManaToClient;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;

//
// mcjty https://wiki.mcjty.eu/modding/index.php?title=Tutorial_1.18_Episode_7
//
public class FlibWorldSavedData extends SavedData {

  private final Map<ChunkPos, ChunkDataStorage> chunkPosData = new HashMap<>();
  private final Random random = new Random();
  //TODO: ticker in new whole thing Keep a counter so that we don't send mana back to the client every tick
  private int syncToClientCounter = 0;

  public FlibWorldSavedData() {}

  public FlibWorldSavedData(CompoundTag tag) {
    ListTag list = tag.getList("mana", Tag.TAG_COMPOUND);
    for (Tag t : list) {
      CompoundTag manaTag = (CompoundTag) t;
      ChunkDataStorage mana = new ChunkDataStorage(manaTag);
      ChunkPos chunkPos = new ChunkPos(manaTag.getInt("x"), manaTag.getInt("z"));
      chunkPosData.put(chunkPos, mana);
    }
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    ListTag list = new ListTag();
    chunkPosData.forEach((chunkPos, mana) -> {
      CompoundTag manaTag = new CompoundTag();
      manaTag.putInt("x", chunkPos.x);
      manaTag.putInt("z", chunkPos.z);
      mana.save(manaTag);
      list.add(manaTag);
    });
    tag.put("mana", list);
    return tag;
  }

  // This function can be used to get access to the mana manager for a given level. It can only be called server-side!
  @Nonnull
  public static FlibWorldSavedData get(Level level) {
    if (level.isClientSide) {
      throw new RuntimeException("Don't access this client-side!");
    }
    // Get the vanilla storage manager from the level
    DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
    // Get the mana manager if it already exists. Otherwise create a new one. Note that both
    // invocations of ManaManager::new actually refer to a different constructor. One without parameters
    // and the other with a CompoundTag parameter
    return storage.computeIfAbsent(FlibWorldSavedData::new, FlibWorldSavedData::new, "data");
  }

  private ChunkDataStorage getDataForPos(BlockPos pos) {
    return getDataForChunk(new ChunkPos(pos));
  }

  private ChunkDataStorage getDataForChunk(ChunkPos chunkPos) {
    return chunkPosData.computeIfAbsent(chunkPos, cp -> new ChunkDataStorage(random.nextInt(4444, 7777))); //default is what? zero? 9999
  }
  //  public int getMana(BlockPos pos) {
  //    ChunkData mana = getManaInternal(pos);
  //    return mana.getMana();
  //  }
  //
  //  public int extractMana(BlockPos pos) {
  //    ChunkData mana = getManaInternal(pos);
  //    int present = mana.getMana();
  //    if (present > 0) {
  //      mana.setMana(present - 1);
  //      // Do not forget to call setDirty() whenever making changes that need to be persisted!
  //      setDirty();
  //      return 1;
  //    }
  //    else {
  //      return 0;
  //    }
  //  }

  public void onWorldTick(Level level) {
    syncToClientCounter--;
    if (syncToClientCounter <= 0) {
      syncToClientCounter = 10;
      level.players().forEach(p -> {
        if (p instanceof ServerPlayer serverPlayer) {
          //sync players own DATA
          PlayerCapabilityStorage playerData = serverPlayer.getCapability(PlayerCapProvider.CYCLIC_PLAYER).orElse(null);
          ChunkDataStorage chunkData = getDataForPos(serverPlayer.blockPosition());
          //
          //
          //and at the same time, get data for the CHUNK you are in and sync at the same time
          //do both instead of once
          //send playerData and chunkData to client 
          //flib packet reg!? 
          PacketRegistry.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketSyncManaToClient(playerData, chunkData));
        }
      });
    }
  }
}
