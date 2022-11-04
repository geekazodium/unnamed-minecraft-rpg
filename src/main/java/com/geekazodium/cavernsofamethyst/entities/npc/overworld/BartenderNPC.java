package com.geekazodium.cavernsofamethyst.entities.npc.overworld;

import com.geekazodium.cavernsofamethyst.entities.npc.InteractableNPC;
import com.geekazodium.cavernsofamethyst.entities.npc.PlayerNPC;
import com.geekazodium.cavernsofamethyst.entities.npc.QuestNPC;
import com.geekazodium.cavernsofamethyst.entities.npc.WorldNPCHandler;
import net.minecraft.world.entity.player.Inventory;
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
