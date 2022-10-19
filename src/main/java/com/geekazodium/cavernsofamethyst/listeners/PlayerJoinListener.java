package com.geekazodium.cavernsofamethyst.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.util.PlayerHandler;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry.HANDLER_VERSION;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage(String.valueOf(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue()));
        PlayerHandler handler = new PlayerHandler(player);
        GameTickHandler.players.put(player, handler);
        updatePlayerItems(player);
        handler.updateStats();
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event){
        GameTickHandler.players.remove(event.getPlayer());
    }

    private void updatePlayerItems(Player player){
        PlayerInventory inventory = player.getInventory();
        for (int s = 0;s<41;s++) {
            ItemStack itemStack = inventory.getItem(s);
            if(itemStack == null){
                continue;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemMeta == null){
                continue;
            }
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            CustomItemHandler customItemHandler = CustomItemHandlerRegistry.get(itemStack);
            if(customItemHandler == null){
                continue;
            }
            customItemHandler.checkIfItemUpdated(inventory,s,container);
        }
    }
}
