package com.geekazodium.cavernsofamethyst.npc;

import com.geekazodium.cavernsofamethyst.Main;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class QuestNPC extends PlayerNPC implements InteractableNPC{
    public static NamespacedKey NPC_ID = new NamespacedKey(Main.getInstance(),"npcid");
    public QuestNPC(String name, Location location, WorldNPCHandler handler) {
        super(name,location,handler);
    }
}
