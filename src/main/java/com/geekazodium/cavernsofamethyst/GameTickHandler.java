package com.geekazodium.cavernsofamethyst;

import com.geekazodium.cavernsofamethyst.holograms.DamageAnimationTickHandler;
import com.geekazodium.cavernsofamethyst.items.CustomProjectileHandler.ProjectileHandler;
import com.geekazodium.cavernsofamethyst.mobs.OverworldMobBehaviorManager;
import com.geekazodium.cavernsofamethyst.npc.overworld.OverworldNPCHandler;
import com.geekazodium.cavernsofamethyst.npc.WorldNPCHandler;
import com.geekazodium.cavernsofamethyst.util.PlayerHandler;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.nio.Buffer;
import java.util.HashMap;

import static com.geekazodium.cavernsofamethyst.Main.minecraftServer;
import static com.geekazodium.cavernsofamethyst.Main.overworld;
import static com.geekazodium.cavernsofamethyst.listeners.EntityInteractListener.playerInteractNpcCooldown;

public class GameTickHandler implements Runnable {
    public static HashMap<Player,Integer> playersAttackCooldown = new HashMap<>();
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

    private static void accept(Player player, PlayerHandler playerHandler) {
        playerHandler.tick();
    }

    @Override
    public void run() {
        players.forEach(GameTickHandler::accept);
        for (Player player : playersAttackCooldown.keySet()) {
            playersAttackCooldown.replace(player,playersAttackCooldown.get(player)-1);
        }
        overworldMobSpawningManager.tick();
        overworldNPCHandler.tick();
        overworldDamageAnimationTickHandler.tick();
        overworldProjectileHandler.tick();
        if(stop){
            return;
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
