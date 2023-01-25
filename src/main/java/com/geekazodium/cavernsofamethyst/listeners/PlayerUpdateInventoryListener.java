package com.geekazodium.cavernsofamethyst.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.items.weapons.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

public class PlayerUpdateInventoryListener implements Listener {
    public static void updatePlayer(Player player) {
        PlayerStatUpdateTask task = new PlayerStatUpdateTask(player);
        Main.minecraftServer.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task, 1);
    }

    @EventHandler
    public void onEvent(InventoryEvent event){
        event.getViewers().forEach(humanEntity -> {
            if(humanEntity instanceof Player player) {
                updatePlayer(player);
            }
        });
    }

    @EventHandler
    public void onEvent(PlayerArmorChangeEvent event){
        updatePlayer(event.getPlayer());
    }

    @EventHandler
    public void onEvent(PlayerItemHeldEvent event){
        PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(event.getPlayer());
        if(playerHandler.isWeaponActive()){
            playerHandler.onWeaponHotKeyEvent(event);
            event.setCancelled(true);
        }
        else updatePlayer(event.getPlayer());
    }

    @EventHandler
    public void onEvent(PlayerDropItemEvent event){
        updatePlayer(event.getPlayer());
    }

    private static class PlayerStatUpdateTask implements Runnable{
        private final Player player;
        public PlayerStatUpdateTask(Player player){
            this.player = player;
        }

        @Override
        public void run() {
            PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(player);
            playerHandler.updateStats();
            CustomItemHandler itemHandler = CustomItemHandlerRegistry.get(player.getInventory().getItem(EquipmentSlot.HAND));
            if(itemHandler instanceof WeaponItemHandler weapon) {
                weapon.setPlayerAttackCooldown(player);
            }
        }
    }
}
