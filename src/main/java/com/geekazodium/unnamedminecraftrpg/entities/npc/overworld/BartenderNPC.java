package com.geekazodium.unnamedminecraftrpg.entities.npc.overworld;

import com.geekazodium.unnamedminecraftrpg.entities.npc.InteractableNPC;
import com.geekazodium.unnamedminecraftrpg.entities.npc.PlayerNPC;
import com.geekazodium.unnamedminecraftrpg.entities.npc.WorldNPCHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class BartenderNPC extends PlayerNPC implements InteractableNPC {//Todo add to starter quest
    public BartenderNPC(Location location, WorldNPCHandler handler) {
        super("ozonic", location, handler);
    }
    @Override
    public void interact(PlayerInteractEntityEvent event) {
        location.getWorld().playSound(location, Sound.ENTITY_VILLAGER_YES,2,1);
    }
}
