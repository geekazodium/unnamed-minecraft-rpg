package com.geekazodium.cavernsofamethyst.players.menus;

import com.geekazodium.cavernsofamethyst.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public abstract class AbstractIntractableMenu implements Menu{
    protected Player player;
    protected Inventory inventory;
    protected int rows = 1;
    protected Component title = Component.text("");
    @Override
    public boolean open(Player player) {
        if(this.player != null) {
            return false;
        }
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        inventory = Bukkit.createInventory(null, rows * 9,title);
        this.player.openInventory(inventory);
        return true;
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event){
        if(!event.getInventory().equals(this.inventory)){
            return;
        }
        event.setCancelled(true);
        onClick(event);
    }

    protected abstract void onClick(InventoryClickEvent event);

    @EventHandler
    public void onPlayerDrag(InventoryDragEvent event){
        if(!event.getInventory().equals(this.inventory)){
            return;
        }
        event.setCancelled(true);
        onDrag(event);
    }

    protected abstract void onDrag(InventoryDragEvent event);

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent event){
        if(!event.getDestination().equals(this.inventory)&&!event.getSource().equals(this.inventory)){
            return;
        }
        event.setCancelled(true);
        onMove(event);
    }

    protected void onMove(InventoryMoveItemEvent event){}

    @EventHandler
    public void onPlayerPickUpItem(InventoryPickupItemEvent event){
        if(!event.getInventory().equals(this.inventory)){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if (!event.getInventory().equals(this.inventory)){
            return;
        }
        close();
    }

    @Override
    public void close() {
        inventory.close();
        inventory.clear();
    }
}
