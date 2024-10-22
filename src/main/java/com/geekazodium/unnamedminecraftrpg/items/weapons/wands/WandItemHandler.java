package com.geekazodium.unnamedminecraftrpg.items.weapons.wands;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.hitbox.Hitbox;
import com.geekazodium.unnamedminecraftrpg.hitbox.HitboxCollisionUtil;
import com.geekazodium.unnamedminecraftrpg.items.weapons.WeaponItemHandler;
import com.geekazodium.unnamedminecraftrpg.util.ParticleUtil;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import com.mojang.math.Quaternion;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class WandItemHandler extends WeaponItemHandler {
    protected static final LinkedList<Hitbox> hitboxes = new LinkedList<>();
    static{
        hitboxes.add(new Hitbox(
                new Vector(0,0,0),
                new Vector(0.5,0.5,0),
                new Vector(0.75,0.75,15),
                Quaternion.ONE,
                (byte)3
        ));
    }
    protected WandItemHandler(int newestVer, String id) {
        super(newestVer, id);
        actions[0]=(PlayerHandler handler)->{
            boolean b = handler.consumeMana(1,this,0);
            if(!b)return false;
            activateNormalAbility(handler);
            return true;
        };
    }

    @Override
    public void useNormalMove(Player player) {
        super.useNormalMove(player);
        playWandAnimation(player);
        HashMap<Entity, Integer> collidedWith = HitboxCollisionUtil.getCollidedWith(
                player.getEyeLocation(),
                hitboxes,
                List.of(player),
                15,15,15
        );
        for (Entity entity:collidedWith.keySet()) {
            if(entity instanceof LivingEntity livingEntity){
                onDamageHit(player,livingEntity,this);
            }
        }
    }

    protected void playWandAnimation(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_NODAMAGE,10,0.5f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP,1,1.5f);
        ParticleUtil.line(
                player.getLocation().getNearbyPlayers(128,128,128),
                Particle.CRIT,1,null,
                player.getEyeLocation(),
                player.getEyeLocation().add(
                        player.getEyeLocation().getDirection().multiply(15)
                ),
                10,0,0,0,0
        );
    }

    public void activateNormalAbility(PlayerHandler player) {
        Player p = player.getPlayer();
        p.getLocation().getWorld().playSound(p.getLocation(),Sound.BLOCK_DISPENSER_LAUNCH,1,1);
        p.getLocation().getWorld().playSound(p.getLocation(),Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,1,0);
        GameTickHandler.getInstance().overworldProjectileHandler.addTickingProjectile(new EnergyOrb(
                p,
                p.getEyeLocation(),
                p.getEyeLocation().getDirection(),
                this
        ));
    }
}
