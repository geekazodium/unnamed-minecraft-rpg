package com.geekazodium.cavernsofamethyst.entities.npc;

import com.geekazodium.cavernsofamethyst.Main;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class WorldNPCHandler {
    private final World world;
    protected final Map<Long,List<NPC>> npcs = new HashMap<>();
    protected final List<NPC> loadedNpcs = new ArrayList<>();
    public Map<UUID,NPC> npcUUIDReference = new HashMap<>();
    private final List<Chunk> waitingLoadChunks = new ArrayList<>();
    boolean init = false;
    public WorldNPCHandler(World world){
        this.world = world;
    }

    public void tick(){
        if(Main.getInstance()!=null) {
            if(!init) {
                waitingLoadChunks.addAll(Arrays.stream(world.getLoadedChunks()).toList());
                init = true;
            }
            waitingLoadChunks.removeIf(chunk -> {
                onLoadChunk(chunk);
                return true;
            });
        }
        loadedNpcs.forEach(NPC::tick);
    }
    public void addNPC(NPC npc){
        long chunkKey = npc.location().getChunk().getChunkKey();
        List<NPC> list;
        if(!npcs.containsKey(chunkKey)){
            list = new ArrayList<>();
            npcs.put(chunkKey, list);
        }else{
            list = npcs.get(chunkKey);
        }
        list.add(npc);
    }
    public void onLoadChunk(@NotNull Chunk chunk) {
        if(Main.getInstance() == null){
            waitingLoadChunks.add(chunk);
        }
        if(chunk.getWorld()!=world){
            //LOGGER.warning("attempted to load npc in wrong world");//TODO make error message show coords
            return;
        }
        List<NPC> list = npcs.get(chunk.getChunkKey());
        if(list == null){
            return;
        }
        for(NPC npc:list) {
            if(loadedNpcs.contains(npc)){
                continue;
            }
            loadedNpcs.add(npc);
        }
    }
    public void onUnloadChunk(@NotNull Chunk chunk){
        if(chunk.getWorld()!=world){
            //LOGGER.warning("attempted to unload npc in wrong world");
            return;
        }
        List<NPC> list = npcs.get(chunk.getChunkKey());
        if(list == null){
            return;
        }
        for(NPC npc:list) {
            npcUUIDReference.remove(npc.getEntity().getUniqueId(),npc);
            loadedNpcs.remove(npc);
            npc.despawn();
        }
    }
}
