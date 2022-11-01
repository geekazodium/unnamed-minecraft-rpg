package com.geekazodium.cavernsofamethyst.util.menus.buttons;

import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface MenuButton {
    ItemStack item();
    void onClick(InventoryClickEvent event);
    ItemStack item(PlayerHandler handler);
}
