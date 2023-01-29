package com.geekazodium.unnamedminecraftrpg.entities.treasure;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.items.weapons.WeaponItemHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Random;

public abstract class LootChestHandler implements TreasureEntity{
    protected static int baseRespawnDuration = 20*60*2;
    protected static int respawnVariation = 1200;
    protected int timeSinceLastOpened = -100;
    protected int timeUntilRespawn = 100;
    protected final Random random = new Random();
    protected boolean isPlaced = false;

    @Override
    public void tick() {
        timeSinceLastOpened +=1;
        if(!isPlaced){
            timeUntilRespawn -= 1;
            if(timeUntilRespawn <= 0){
                isPlaced = true;
                location().getBlock().setType(Material.CHEST);
            }
        }else{
            location().getWorld().getNearbyPlayers(location(),10)
                    .forEach(p -> p.spawnParticle(Particle.FIREWORKS_SPARK,location().toCenterLocation(),1,0.5,0.5,0.5,0.2));
        }
    }

    @Override
    public void interact(Player player) {
        isPlaced = false;
        location().getWorld().playEffect(location(), Effect.WITHER_BREAK_BLOCK,0,10);
        BlockData blockData = location().getBlock().getBlockData();
        location().getWorld().getNearbyPlayers(location(),10)
                .forEach(p -> p.spawnParticle(Particle.BLOCK_CRACK,location(),20, blockData));
        location().getBlock().setType(Material.AIR);
        timeUntilRespawn = random.nextInt(baseRespawnDuration,baseRespawnDuration+respawnVariation);
        timeSinceLastOpened = -timeUntilRespawn;
    }

    protected void openChestInventory(Player player, Inventory inventory){
        PlayerHandler handler = GameTickHandler.getPlayerHandler(player);
        handler.setAllowSendInventoryUpdatePacket(true);
        PlayerHandler.sendInventoryUpdatePacket(((CraftPlayer) player).getHandle().networkManager, ((CraftInventoryPlayer) player.getInventory()).getInventory());
        PlayerCloseInventoryListener closeInventoryListener = new PlayerCloseInventoryListener(handler, inventory);
        Bukkit.getPluginManager().registerEvents(closeInventoryListener, Main.getInstance());
        player.openInventory(inventory);
    }

    private record PlayerCloseInventoryListener(PlayerHandler player, Inventory inventory) implements Listener {
        @EventHandler
        public void onPlayerDisconnect(PlayerConnectionCloseEvent event){
            if(event.getPlayerUniqueId()!=player.getPlayer().getUniqueId())return;
            HandlerList.unregisterAll(this);
            player.setAllowSendInventoryUpdatePacket(false);
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
    public boolean canInteract(Player player) {
        return isPlaced;
    }
}
