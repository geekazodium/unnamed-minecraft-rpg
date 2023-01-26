package com.geekazodium.unnamedminecraftrpg.util.menus.buttons;

import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuButton {
    ItemStack item();
    void onClick(InventoryClickEvent event);
    ItemStack item(PlayerHandler handler);
}
