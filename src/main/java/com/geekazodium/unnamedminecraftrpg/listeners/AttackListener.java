package com.geekazodium.unnamedminecraftrpg.listeners;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.items.CustomItemHandler;
import com.geekazodium.unnamedminecraftrpg.items.weapons.WeaponItemHandler;
import com.geekazodium.unnamedminecraftrpg.util.EntityDamageUtil;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import static com.geekazodium.unnamedminecraftrpg.entities.npc.PlayerNPC.isNPCKey;

public class AttackListener implements Listener {

    @EventHandler
    public void onEvent(PlayerArmSwingEvent event){
        Player player = event.getPlayer();
        PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(player);
        CustomItemHandler itemHandler = playerHandler.getActiveItemHandler();
        if (itemHandler == null)return;
        if(itemHandler instanceof WeaponItemHandler) deferToWeaponLeftClick(playerHandler,itemHandler,event);
        else {
            if(playerHandler.isOnCooldown())return;
            itemHandler.onLeftClickMainHand(event);
        }
        event.setCancelled(true);
//        if(itemInMainHand == null)return;
//        CustomItemHandler customItemHandler = CustomItemHandlerRegistry.get(itemInMainHand);
//        if(playerHandler.isWeaponActive()||customItemHandler instanceof WeaponItemHandler){
//            deferToWeaponLeftClick(playerHandler,customItemHandler,event);
//            return;
//        }
//        if(customItemHandler==null)return;
//        event.setCancelled(true);
//        if(!playerHandler.isOnCooldown()) {
//            customItemHandler.onLeftClickMainHand(event);
//        }
    }
    @EventHandler
    public void onEvent(PlayerInteractEvent event){
        PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(event.getPlayer());
        if(playerHandler.checkForDuplicateRightClick(event)) return;
        CustomItemHandler itemHandler = playerHandler.getActiveItemHandler();
        if(itemHandler==null)return;
        if(!event.getAction().isRightClick())return;
        if(itemHandler instanceof WeaponItemHandler) return;
        else itemHandler.onRightClickMainHand(event);
        event.setCancelled(true);
//        if(itemInMainHand == null)return;
//        if(!event.getAction().isRightClick())return;
//        CustomItemHandler customItemHandler = CustomItemHandlerRegistry.get(itemInMainHand);
//        if(playerHandler.isWeaponActive()||customItemHandler instanceof WeaponItemHandler){
//            deferToWeaponRightClick(playerHandler,customItemHandler,event);
//            return;
//        }
//        if(customItemHandler==null)return;
//        event.setCancelled(true);
//        customItemHandler.onRightClickMainHand(event);
    }

    private void deferToWeaponLeftClick(PlayerHandler playerHandler, CustomItemHandler customItemHandler,PlayerArmSwingEvent event){
        event.setCancelled(true);
        if(playerHandler.isWeaponActive()){
            ItemStack item = playerHandler.getPlayer().getInventory().getItemInMainHand();
            if(item.getType() == Material.BARRIER){
                playerHandler.lowerWeapon();
                return;
            }
            if(customItemHandler!=null && !playerHandler.isOnCooldown()) customItemHandler.onLeftClickMainHand(event);
        }
        playerHandler.readyWeapon();
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
            if (GameTickHandler.getPlayerHandler(player).getActiveItemHandler() != null) {
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
    public void onEvent(ProjectileHitEvent event){
        event.getEntity().remove();
    }
}
