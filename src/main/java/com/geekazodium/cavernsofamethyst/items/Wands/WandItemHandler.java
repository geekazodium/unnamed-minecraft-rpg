package com.geekazodium.cavernsofamethyst.items.Wands;

import com.geekazodium.cavernsofamethyst.hitbox.Hitbox;
import com.geekazodium.cavernsofamethyst.hitbox.HitboxCollisionUtil;
import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.util.ParticleUtil;
import com.geekazodium.cavernsofamethyst.util.Quaternion;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Particle;
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
                Quaternion.IDENTITY,
                (byte)3
        ));
    }
    protected WandItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }

    @Override
    public void onLeftClickMainHand(PlayerArmSwingEvent event) {
        super.onLeftClickMainHand(event);
        Player player = event.getPlayer();
        playWandAnimation(player);
        HashMap<Entity, Integer> collidedWith = HitboxCollisionUtil.getCollidedWith(
                event.getPlayer().getEyeLocation(),
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
}
