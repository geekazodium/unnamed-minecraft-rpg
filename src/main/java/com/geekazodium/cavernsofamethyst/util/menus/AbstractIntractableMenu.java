package com.geekazodium.cavernsofamethyst.util.menus;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

public abstract class AbstractIntractableMenu implements Menu,Runnable{
    protected boolean open = false;
    protected Player player;
    protected PlayerHandler handler;
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
        open = true;
        handler = GameTickHandler.getPlayerHandler(player);
        this.handler.scheduleAction(this, 1);
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
        if(!open){
            return;
        }
        close();
    }

    @Override
    public void close() {
        open = false;
        inventory.close();
        inventory.clear();
    }

    @Override
    public void run() {
        if(!open) {
            return;
        }
        handler.scheduleAction(this, 1);
    }
}
