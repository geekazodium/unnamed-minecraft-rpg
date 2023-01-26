package com.geekazodium.unnamedminecraftrpg.entities.npc;

import com.geekazodium.unnamedminecraftrpg.Main;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public abstract class QuestNPC extends PlayerNPC implements InteractableNPC{
    public static NamespacedKey NPC_ID = new NamespacedKey(Main.getInstance(),"npcid");
    public QuestNPC(String name, Location location, WorldNPCHandler handler) {
        super(name,location,handler);
    }
}
