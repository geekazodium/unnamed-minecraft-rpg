package com.geekazodium.unnamedminecraftrpg.util.menus;

import com.geekazodium.unnamedminecraftrpg.util.menus.buttons.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class AbstractButtonMenu extends AbstractIntractableMenu{
    protected HashMap<Integer,MenuButton> buttons = new HashMap<>();
    @Override
    public boolean open(Player player) {
        boolean b = super.open(player);
        buttons.forEach((integer,menuButton) ->{
            inventory.setItem(integer,menuButton.item());
        });
        return b;
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        MenuButton button = buttons.get(event.getSlot());
        if(button == null){
            return;
        }
        button.onClick(event);
    }

    @Override
    protected void onDrag(InventoryDragEvent event) {

    }

    @Override
    public void run() {
        buttons.forEach((integer,menuButton) ->{
            ItemStack item = menuButton.item(handler);
            inventory.setItem(integer, item);
        });
        super.run();
    }
}
