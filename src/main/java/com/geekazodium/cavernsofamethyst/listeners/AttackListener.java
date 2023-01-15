package com.geekazodium.cavernsofamethyst.listeners;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.Map;

import static com.geekazodium.cavernsofamethyst.entities.npc.PlayerNPC.isNPCKey;

public class AttackListener implements Listener {

    @EventHandler
    public void onEvent(PlayerArmSwingEvent event){
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        if(itemInMainHand == null)return;
        CustomItemHandler customItemHandler = CustomItemHandlerRegistry.get(itemInMainHand);
        if(customItemHandler!=null){
            event.setCancelled(true);
            if(!GameTickHandler.getPlayerHandler(player).isOnCooldown()) {
                customItemHandler.onLeftClickMainHand(event);
            }
        }
    }
    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if(damager.getPersistentDataContainer().getOrDefault(PlayerHandler.VISUAL_ONLY_KEY,PersistentDataType.BYTE,(byte)0) != (byte)0){
            event.setCancelled(true);
            return;
        }
        Entity victim = event.getEntity();
        if (damager instanceof Player player) {
            if(victim instanceof Player){
                event.setCancelled(true);
            }
            if (CustomItemHandlerRegistry.get(player.getInventory().getItemInMainHand()) != null) {
                event.setCancelled(true);
            }
        }else{
            if(damager instanceof LivingEntity livingEntityDamager){
                if(damager.getPersistentDataContainer().getOrDefault(isNPCKey, PersistentDataType.BYTE,(byte) 0) == 1){
                    event.setCancelled(true);
                    return;
                }
                if(victim instanceof Player player){
                    EntityDamageUtil.onEntityDamagePlayer(livingEntityDamager,player);
                    event.setCancelled(true);
                }else{
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onEvent(ProjectileCollideEvent event){
        Projectile projectile = event.getEntity();
        Entity collidedWith = event.getCollidedWith();
        ProjectileSource shooter = projectile.getShooter();
        if(collidedWith instanceof Player){
            if(shooter instanceof Player){
                event.setCancelled(true);
            }
        }else if(collidedWith instanceof LivingEntity livingEntity){
            if(shooter instanceof Player player){
                event.setCancelled(true);
                EntityDamageUtil.onPlayerProjectileDamageEntity(player, projectile.getPersistentDataContainer(), livingEntity);
                projectile.remove();
            }
        }
    }
    @EventHandler
    public void onEvent(PlayerInteractEvent event){
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(event.getPlayer());
        if(playerHandler.checkForDuplicateRightClick(event)) return;
        if(itemInMainHand == null)return;
        if(!event.getAction().isRightClick())return;
        CustomItemHandler customItemHandler = CustomItemHandlerRegistry.get(itemInMainHand);
        if(customItemHandler!=null){
            event.setCancelled(true);
            customItemHandler.onRightClickMainHand(event);
        }
    }
    @EventHandler
    public void onEvent(ProjectileHitEvent event){
        event.getEntity().remove();
    }
}
