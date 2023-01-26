package com.geekazodium.unnamedminecraftrpg;

import com.geekazodium.unnamedminecraftrpg.entities.mobs.OverworldMobBehaviorManager;
import com.geekazodium.unnamedminecraftrpg.entities.npc.WorldNPCHandler;
import com.geekazodium.unnamedminecraftrpg.entities.npc.overworld.OverworldNPCHandler;
import com.geekazodium.unnamedminecraftrpg.entities.treasure.TreasureEntityRegistry;
import com.geekazodium.unnamedminecraftrpg.entities.holograms.DamageAnimationTickHandler;
import com.geekazodium.unnamedminecraftrpg.items.projectiles.ProjectileHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.HashMap;

import static com.geekazodium.unnamedminecraftrpg.Main.minecraftServer;
import static com.geekazodium.unnamedminecraftrpg.Main.overworld;

public class GameTickHandler implements Runnable {
    public final WorldNPCHandler overworldNPCHandler;
    private final OverworldMobBehaviorManager overworldMobSpawningManager = new OverworldMobBehaviorManager();
    public final DamageAnimationTickHandler overworldDamageAnimationTickHandler = new DamageAnimationTickHandler(overworld);
    private static GameTickHandler instance;
    public static HashMap<Player, PlayerHandler> players = new HashMap<>();
    private boolean stop = false;
    public final ProjectileHandler overworldProjectileHandler = new ProjectileHandler(overworld);

    public GameTickHandler(){
        overworldNPCHandler = new OverworldNPCHandler();
        instance = this;
    }

    public static PlayerHandler getPlayerHandler(Player player){
        return players.get(player);
    }

    private static void accept(Player player, PlayerHandler playerHandler) {
        playerHandler.tick();
    }

    @Override
    public void run() {
        try {
            players.forEach(GameTickHandler::accept);
            overworldMobSpawningManager.tick();
            overworldNPCHandler.tick();
            overworldDamageAnimationTickHandler.tick();
            overworldProjectileHandler.tick();
            TreasureEntityRegistry.tickHandlers();
            if (stop) {
                return;
            }
        }catch(Exception e){
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                if(player.hasPermission("operator")){
                    player.sendMessage(e.getMessage());
                    for (StackTraceElement s : e.getStackTrace()) {
                        player.sendMessage(s.toString());
                    }
                }
            });
        }
        minecraftServer.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),this,1);
    }

    public void onClose(){
        stop = true;
        overworldDamageAnimationTickHandler.close();
        overworldMobSpawningManager.clear();
        for (Chunk chunk : overworld.getLoadedChunks()) {
            overworldNPCHandler.onUnloadChunk(chunk);
        }
    }

    public static GameTickHandler getInstance(){
        return instance;
    }

    public void onLoadChunk(ChunkLoadEvent event) {
        overworldNPCHandler.onLoadChunk(event.getChunk());
    }

    public void onUnloadChunk(ChunkUnloadEvent event) {
        overworldNPCHandler.onUnloadChunk(event.getChunk());
    }
}
