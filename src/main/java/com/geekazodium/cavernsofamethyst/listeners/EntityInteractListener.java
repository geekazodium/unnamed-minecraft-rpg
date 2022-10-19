package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.npc.InteractableNPC;
import com.geekazodium.cavernsofamethyst.npc.NPC;
import com.geekazodium.cavernsofamethyst.util.PlayerHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import static com.geekazodium.cavernsofamethyst.Main.overworld;
import static com.geekazodium.cavernsofamethyst.npc.QuestNPC.NPC_ID;

public class EntityInteractListener implements Listener {
    public static final NamespacedKey playerInteractNpcCooldown = new NamespacedKey(Main.getInstance(),"interactNpcCooldown");
    @EventHandler
    public void onEvent(PlayerInteractEntityEvent event){
        event.setCancelled(true);
        Entity rightClicked = event.getRightClicked();
        //PersistentDataContainer container = rightClicked.getPersistentDataContainer();
        // String npcId = container.getOrDefault(NPC_ID, PersistentDataType.STRING, "");
        if(rightClicked.getWorld() == overworld) {
            NPC npc = GameTickHandler.getInstance().overworldNPCHandler.npcUUIDReference.get(rightClicked.getUniqueId());
            if(npc instanceof InteractableNPC interactable){
                Player player = event.getPlayer();
                PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
                Integer cooldown = persistentDataContainer.getOrDefault(playerInteractNpcCooldown, PersistentDataType.INTEGER, 0);
                if(cooldown >0){
                    return;
                }
                persistentDataContainer.set(playerInteractNpcCooldown,PersistentDataType.INTEGER,2);
                if(Main.getInstance() == null){
                    return;
                }
                interactable.interact(event);
            }
        }
    }
}
