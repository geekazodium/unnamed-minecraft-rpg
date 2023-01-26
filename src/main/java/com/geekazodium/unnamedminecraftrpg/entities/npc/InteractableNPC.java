package com.geekazodium.unnamedminecraftrpg.entities.npc;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface InteractableNPC extends NPC{
    void interact(PlayerInteractEntityEvent event);
}
