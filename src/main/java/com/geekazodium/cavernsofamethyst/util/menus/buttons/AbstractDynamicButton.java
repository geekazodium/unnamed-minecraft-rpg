package com.geekazodium.cavernsofamethyst.util.menus.buttons;

import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractDynamicButton extends AbstractMenuButton {

    @Override
    public abstract ItemStack item(PlayerHandler handler);
}
