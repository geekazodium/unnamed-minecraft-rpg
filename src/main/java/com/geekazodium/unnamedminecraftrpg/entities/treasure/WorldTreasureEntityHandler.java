package com.geekazodium.unnamedminecraftrpg.entities.treasure;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.items.CustomItemHandlerRegistry;
import com.geekazodium.unnamedminecraftrpg.items.weapons.WeaponItemHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldTreasureEntityHandler implements Listener {
    public final World world;
    private final Map<Long, List<TreasureEntity>> worldTreasureEntities = new HashMap<>();
    public final List<Long> loadedChunks = new ArrayList<>();
    private final Map<Location,TreasureEntity> treasureEntityLocations = new HashMap<>();
    public WorldTreasureEntityHandler(World world){
        this.world = world;
    }
    public void tick(){
        loadedChunks.forEach(aLong -> {
            if(!worldTreasureEntities.containsKey(aLong)){
                return;
            }
            List<TreasureEntity> entities = worldTreasureEntities.get(aLong);
            entities.forEach(TreasureEntity::tick);
        });
    }
    public void put(TreasureEntity entity){
        Location location = entity.location();
        if(location == null){
            return;
        }
        long chunkKey = location.getChunk().getChunkKey();
        List<TreasureEntity> chunkEntities;
        if(worldTreasureEntities.containsKey(chunkKey)){
            chunkEntities = worldTreasureEntities.get(chunkKey);
        }else{
            chunkEntities = new ArrayList<>();
            worldTreasureEntities.put(chunkKey,chunkEntities);
        }
        chunkEntities.add(entity);
        treasureEntityLocations.put(location,entity);
    }

    @EventHandler
    public void onEvent(PlayerInteractEvent event){
        Block clickedBlock = event.getClickedBlock();
        if(clickedBlock ==null)return;
        TreasureEntity treasureEntity = treasureEntityLocations.get(event.getClickedBlock().getLocation());
        if(treasureEntity == null)return;
        if(event.getAction()== Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
        }else return;
        Player player = event.getPlayer();
        if(!treasureEntity.canInteract(player))return;
        PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(player);
        if(playerHandler.isWeaponActive())playerHandler.setAllowSendInventoryUpdatePacket(true);
        treasureEntity.interact(player);
    }
    @EventHandler
    public void onLoadChunk(ChunkLoadEvent event){
        loadedChunks.add(event.getChunk().getChunkKey());
    }
    @EventHandler
    public void onUnloadChunk(ChunkUnloadEvent event){
        loadedChunks.remove(event.getChunk().getChunkKey());
    }

    public void load() {
        @NotNull Chunk[] loadedChunks_ = world.getLoadedChunks();
        for (Chunk chunk : loadedChunks_) {
            loadedChunks.add(chunk.getChunkKey());
        }
        Bukkit.getServer().getPluginManager().registerEvents(this,Main.getInstance());
    }

    public void unload(){
        loadedChunks.clear();
        HandlerList.unregisterAll(this);
    }
}
