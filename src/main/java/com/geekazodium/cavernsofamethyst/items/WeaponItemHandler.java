package com.geekazodium.cavernsofamethyst.items;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.geekazodium.cavernsofamethyst.GameTickHandler.playersAttackCooldown;

public abstract class WeaponItemHandler extends CustomItemHandler{
    protected int attackDelayTier = 4;

    protected WeaponItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }
    protected void onDamageHit(Player player, LivingEntity entity,WeaponItemHandler itemHandler) {
        EntityDamageUtil.onPlayerDamageEntity(player,entity,itemHandler);
    }

    @Override
    public void onLeftClickMainHand(PlayerArmSwingEvent event) {
        Player player = event.getPlayer();
        setPlayerAttackCooldown(player);
    }

    public void setPlayerAttackCooldown(Player player){
        int delay = Math.max(1,Math.min(attackDelayTier,8));
        delay *= 5;
        if(playersAttackCooldown.containsKey(player)){
            playersAttackCooldown.replace(player, delay);
        }else {
            playersAttackCooldown.put(player, delay);
        }
        player.setCooldown(player.getInventory().getItemInMainHand().getType(),Math.max(0, GameTickHandler.playersAttackCooldown.getOrDefault(player,0)));
    }

    public int fireBaseDamage(){
        return 0;
    }

    public int earthBaseDamage(){
        return 0;
    }

    public int waterBaseDamage(){
        return 0;
    }
    @Override
    public boolean isPlayerOnCD(Player player) {
        return playersAttackCooldown.getOrDefault(player,0)>0;
    }
}
