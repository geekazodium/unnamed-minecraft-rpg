package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

public class PlayerShiftListener implements Listener {
    @EventHandler
    public void onEvent(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(player.isSneaking()) {
            GameTickHandler.players.get(player).onSneaked();
        }
    }
}
