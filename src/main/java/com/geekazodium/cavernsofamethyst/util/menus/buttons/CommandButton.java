package com.geekazodium.cavernsofamethyst.util.menus.buttons;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CommandButton extends AbstractMenuButton{
    private final String command;
    public CommandButton(String command){
        this.command = command;
    }
    @Override
    public void onClick(InventoryClickEvent event) {
        HumanEntity entity = event.getViewers().get(0);
        if (entity instanceof Player player) {
            Bukkit.dispatchCommand(player, command);
        }
    }
}
