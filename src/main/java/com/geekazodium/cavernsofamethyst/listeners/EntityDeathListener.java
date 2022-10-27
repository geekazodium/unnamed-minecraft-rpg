package com.geekazodium.cavernsofamethyst.listeners;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.hitbox.EntityHurtBoxUtil;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class EntityDeathListener implements Listener {
    public static final NamespacedKey ENTITY_XP_KEY = new NamespacedKey(Main.getInstance(),"xp_key");
    @EventHandler
    public void onEvent(EntityDeathEvent event){
        PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
        List<Player> damagedBy = EntityDamageUtil.getEntityDamagedBy(container);
        for (Player player : damagedBy) {
            player.giveExp(container.getOrDefault(ENTITY_XP_KEY, PersistentDataType.INTEGER,0));
        }
        EntityHurtBoxUtil.remove(event.getEntity());
    }

    @EventHandler
    public void onEvent(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        //player.sendMessage("respawned");
        Main.minecraftServer.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> GameTickHandler.getPlayerHandler(player).respawnPlayer(),
                1
        );
    }
}
