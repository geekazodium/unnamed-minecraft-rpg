package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.hitbox.EntityHurtBoxUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEvent(EntityDeathEvent event){
        EntityHurtBoxUtil.remove(event.getEntity());
    }
}
