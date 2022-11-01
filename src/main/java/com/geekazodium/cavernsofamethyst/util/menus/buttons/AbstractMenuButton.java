package com.geekazodium.cavernsofamethyst.util.menus.buttons;

import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractMenuButton implements MenuButton{
    public AbstractMenuButton(){
    }
    @Override
    public ItemStack item() {
        return new ItemStack(Material.BARRIER,1);
    }

    @Override
    public ItemStack item(PlayerHandler handler) {
        return item();
    }
}
