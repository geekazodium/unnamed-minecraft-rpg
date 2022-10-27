package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerShiftListener implements Listener {
    @EventHandler
    public void onEvent(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(player.isSneaking()) {
            GameTickHandler.players.get(player).onSneaked();
        }
    }
}
