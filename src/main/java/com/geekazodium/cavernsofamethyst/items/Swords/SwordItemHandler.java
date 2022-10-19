package com.geekazodium.cavernsofamethyst.items.Swords;

import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.util.VecUtil;
import com.geekazodium.cavernsofamethyst.hitbox.Hitbox;
import com.geekazodium.cavernsofamethyst.hitbox.HitboxCollisionUtil;
import com.geekazodium.cavernsofamethyst.util.Quaternion;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class SwordItemHandler extends WeaponItemHandler {
    public static Random random = new Random();
    protected static final LinkedList<Hitbox> hitboxes = new LinkedList<>();
    static{
        hitboxes.add(new Hitbox(
                new Vector(0,0,0),
                new Vector(0.5,0.5,0),
                new Vector(3,0.75,3),
                Quaternion.IDENTITY,
                (byte)3
        ));
    }
    protected SwordItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }

    @Override
    public void onLeftClickMainHand(PlayerArmSwingEvent event) {
        super.onLeftClickMainHand(event);
        Player player = event.getPlayer();
        playSwordAnimation(player);
        HashMap<Entity, Integer> collidedWith = HitboxCollisionUtil.getCollidedWith(
                event.getPlayer().getEyeLocation(),
                hitboxes,
                List.of(player)
        );
        for (Entity entity:collidedWith.keySet()) {
            if(entity instanceof LivingEntity livingEntity){
                onDamageHit(player,livingEntity,this);
            }
        }
    }
    protected void playSwordAnimation(Player player){
        Location location = player.getEyeLocation();
        location.add(
                VecUtil.getVectorFor(-location.getPitch()-15, -location.getYaw(), 1.75,true)
        );
        player.spawnParticle(Particle.SWEEP_ATTACK,location,1,0,0,0,0);
        Location location_ = player.getEyeLocation();
        location_.add(location_.getDirection().multiply(1.5));
        for(Player otherPlayer:player.getWorld().getNearbyPlayers(player.getLocation(),128)){
            if(otherPlayer == player){
                continue;
            }
            otherPlayer.spawnParticle(Particle.SWEEP_ATTACK,location_,1,0,0,0,0);
        }
    }
}
