package com.geekazodium.cavernsofamethyst.players.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface Menu extends Listener {
    boolean open(Player player);
    void close();
}
