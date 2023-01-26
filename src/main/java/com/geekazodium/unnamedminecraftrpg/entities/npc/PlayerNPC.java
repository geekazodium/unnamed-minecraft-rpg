package com.geekazodium.unnamedminecraftrpg.entities.npc;

import com.destroystokyo.paper.entity.ai.GoalType;
import com.geekazodium.unnamedminecraftrpg.Main;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public abstract class PlayerNPC implements NPC{
    public static final NamespacedKey isNPCKey = new NamespacedKey(Main.getInstance(),"isEntityNPC");
    private final Random random = new Random();
    int focusTime = 0;
    Player focusedPlayer = null;
    protected PlayerDisguise disguise;
    protected final WorldNPCHandler handler;
    protected String name;
    protected Location location;
    public PlayerNPC(String name, Location location,WorldNPCHandler handler){
        this.name = name;
        this.location = location;
        this.handler = handler;
    }
    protected Husk npc;
    public void spawn(){
        if(!location.getChunk().isLoaded()){
            return;
        }
        //location.getChunk().addPluginChunkTicket(Main.getInstance());
        //Main.LOGGER.log(Level.INFO, String.valueOf(location.getChunk().isEntitiesLoaded()));
        npc = (Husk) location.getWorld().spawnEntity(location, org.bukkit.entity.EntityType.HUSK,false);
        npc.setPersistent(true);
        npc.customName(Component.text("npc"));
        npc.setCanPickupItems(false);
        npc.getPersistentDataContainer().set(isNPCKey, PersistentDataType.BYTE,(byte) 1);
        npc.setInvulnerable(true);
        npc.setTarget(null);
        npc.setGravity(false);
        npc.setCollidable(false);
        Bukkit.getMobGoals().removeAllGoals(npc, GoalType.TARGET);
        //((net.minecraft.world.entity.monster.Husk) npc).getClass().metho
        //npc.setAI(true);
        //location.getChunk().removePluginChunkTicket(Main.getInstance());
        disguise = new PlayerDisguise(name);
        DisguiseAPI.disguiseEntity(npc, disguise);
        if(!npc.isDead()){
            handler.npcUUIDReference.put(npc.getUniqueId(),this);
        }
    }

    @Override
    public Entity getEntity() {
        return npc;
    }

    @Override
    public void despawn() {
        DisguiseAPI.undisguiseToAll(npc);
        npc.remove();
        npc = null;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public void tick() {
        if(focusedPlayer == null|| focusTime>50||focusedPlayer.getLocation().distance(location)>10) {
            focusedPlayer = null;
            Player[] nearbyPlayers = location.getNearbyPlayers(10).toArray(new Player[0]);
            if (nearbyPlayers.length>0) {
                focusedPlayer = nearbyPlayers[random.nextInt(0,nearbyPlayers.length)];
                focusTime = 0;
            }
        }
        focusTime +=1;
        if(npc==null||npc.isDead()){
            spawn();
        }
        if(focusedPlayer != null) {
            npc.lookAt(focusedPlayer);
            npc.setRotation(npc.getLocation().getYaw(),npc.getLocation().getPitch());
        }else{
            npc.setRotation(location.getYaw(),location.getPitch());
        }
    }
}
