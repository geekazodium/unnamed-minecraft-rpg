package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.hitbox.EntityHurtBoxUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEvent(EntityDeathEvent event){
        EntityHurtBoxUtil.remove(event.getEntity());
    }

    @EventHandler
    public void onEvent(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        player.sendMessage("respawned");
        Main.minecraftServer.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> GameTickHandler.getPlayerHandler(player).respawnPlayer(),
                1
        );
    }
}
