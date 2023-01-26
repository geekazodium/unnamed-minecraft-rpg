package com.geekazodium.unnamedminecraftrpg.util.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface Menu extends Listener {
    boolean open(Player player);
    void close();
}
