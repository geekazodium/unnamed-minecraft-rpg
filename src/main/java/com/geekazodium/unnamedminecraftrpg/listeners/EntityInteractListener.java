package com.geekazodium.unnamedminecraftrpg.listeners;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.entities.npc.InteractableNPC;
import com.geekazodium.unnamedminecraftrpg.entities.npc.NPC;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.geekazodium.unnamedminecraftrpg.Main.overworld;

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
