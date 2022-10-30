package com.geekazodium.cavernsofamethyst.players.menus;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class PlayerSkillMenu extends AbstractIntractableMenu{
    public PlayerSkillMenu(){
        title = Component.text("player skills");
        rows = 3;
    }
    @Override
    protected void onClick(InventoryClickEvent event) {
    }

    @Override
    protected void onDrag(InventoryDragEvent event) {

    }
}
