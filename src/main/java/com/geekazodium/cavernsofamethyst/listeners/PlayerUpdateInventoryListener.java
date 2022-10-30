package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.items.weapons.WeaponItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerUpdateInventoryListener implements Listener {
    @EventHandler
    public void onEvent(PlayerItemHeldEvent event){
        PlayerStatUpdateTask task = new PlayerStatUpdateTask(event.getPlayer());
        Main.minecraftServer.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),task,1);
        //GameTickHandler.playersAttackCooldown.put(event.getPlayer());
    }

    private static class PlayerStatUpdateTask implements Runnable{
        private final Player player;
        public PlayerStatUpdateTask(Player player){
            this.player = player;
        }

        @Override
        public void run() {
            GameTickHandler.players.get(player).updateStats();
            CustomItemHandler itemHandler = CustomItemHandlerRegistry.get(player.getInventory().getItem(EquipmentSlot.HAND));
            if(itemHandler instanceof WeaponItemHandler weapon){
                weapon.setPlayerAttackCooldown(player);
            }
        }
    }
}
