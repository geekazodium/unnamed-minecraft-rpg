package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class PlayerLevelUpListener implements Listener {
    @EventHandler
    public void onEvent(PlayerLevelChangeEvent event){
        if(event.getNewLevel()>event.getOldLevel()){
            GameTickHandler.getPlayerHandler(event.getPlayer()).onLevelUpdate(event.getNewLevel());
        }
    }
}
