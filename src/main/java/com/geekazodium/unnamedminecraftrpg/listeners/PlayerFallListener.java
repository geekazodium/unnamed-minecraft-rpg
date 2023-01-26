package com.geekazodium.unnamedminecraftrpg.listeners;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallListener implements Listener {
    @EventHandler
    public void onEvent(EntityDamageEvent event){
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL){
            return;
        }
        if(event.getEntity() instanceof Player player){
            PlayerHandler handler = GameTickHandler.getPlayerHandler(player);
            handler.onFallEvent(event);
        }
    }
}
