package com.geekazodium.unnamedminecraftrpg.entities.treasure;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.items.CustomItemHandlerRegistry;
import com.geekazodium.unnamedminecraftrpg.items.weapons.WeaponItemHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import com.geekazodium.unnamedminecraftrpg.util.RandomUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class RandomLootChest extends LootChestHandler{
    private final HashMap<ItemStack,Float> lootTable = new HashMap<>();
    private final Location location;
    public RandomLootChest(Location location){
        lootTable.put(new ItemStack(Material.EMERALD,1),0.1f);
        lootTable.put(CustomItemHandlerRegistry.getFromId("wooden_bow").getNewestItem(),0.005f);
        lootTable.put(CustomItemHandlerRegistry.getFromId("warm_blade").getNewestItem(),0.005f);
        lootTable.put(CustomItemHandlerRegistry.getFromId("icicle").getNewestItem(),0.005f);
        this.location = location;
        baseRespawnDuration = 1000;
        respawnVariation = 200;
    }

    @Override
    public void interact(Player player) {
        super.interact(player);
        Inventory inventory = Bukkit.createInventory(null, 27, Component.empty());
        for (int i = 0; i<27;i++) {
            ItemStack itemStack = RandomUtil.randomPickFromWeighted(lootTable, random);
            if(itemStack == null){
                continue;
            }
            inventory.setItem(i, itemStack.clone());
        }
        //inventory.setItem(13,newestItem);
        openChestInventory(player,inventory);
    }

    private void openChestInventory(Player player, Inventory inventory){
        PlayerHandler handler = GameTickHandler.getPlayerHandler(player);
        handler.setAllowSendInventoryUpdatePacket(true);
        PlayerHandler.sendInventoryUpdatePacket(((CraftPlayer) player).getHandle().networkManager, ((CraftInventoryPlayer) player.getInventory()).getInventory());
        PlayerCloseInventoryListener closeInventoryListener = new PlayerCloseInventoryListener(handler, inventory);
        Bukkit.getPluginManager().registerEvents(closeInventoryListener,Main.getInstance());
        player.openInventory(inventory);
    }

    private record PlayerCloseInventoryListener(PlayerHandler player, Inventory inventory) implements Listener{
        @EventHandler
        public void onPlayerDisconnect(PlayerConnectionCloseEvent event){
            if(event.getPlayerUniqueId()!=player.getPlayer().getUniqueId())return;
            HandlerList.unregisterAll(this);
        }

        @EventHandler
        public void onPlayerCloseInventory(InventoryCloseEvent event){
            if(event.getPlayer()!=player.getPlayer())return;
            if(event.getInventory()!=inventory)return;
            player.sendVisualOnlyWeaponHotbar(player.getPlayer().getInventory(), (WeaponItemHandler) player.getActiveItemHandler());
            player.setAllowSendInventoryUpdatePacket(false);
        }
    }

    @Override
    public Location location() {
        return location;
    }
}
