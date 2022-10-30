package com.geekazodium.cavernsofamethyst.items.weapons.swords;

import com.geekazodium.cavernsofamethyst.hitbox.Hitbox;
import com.geekazodium.cavernsofamethyst.hitbox.HitboxCollisionUtil;
import com.geekazodium.cavernsofamethyst.items.weapons.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import com.geekazodium.cavernsofamethyst.util.Quaternion;
import com.geekazodium.cavernsofamethyst.util.VecUtil;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

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
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP,1,1);
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

    @Override
    public void activateNormalAbility(PlayerHandler player) {
        if(!player.consumeMana(1)){
            return;
        }
        player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP,1,2);
        player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ARROW_SHOOT,1,1);
        Player p = player.getPlayer();
        Vector direction = p.getEyeLocation().getDirection();
        p.setVelocity(direction.setY(direction.getY()/10).normalize().add(new Vector(0,0.4,0)));
        p.setInvulnerable(true);
        player.scheduleAction(new DashAttack(player,this),1);
    }

    private static class DashAttack implements Runnable{
        int attackFrames = 20;
        protected static final LinkedList<Hitbox> dashHitboxes = new LinkedList<>();
        protected static final LinkedList<Hitbox> triggerHitboxes = new LinkedList<>();
        static{
            dashHitboxes.add(new Hitbox(
                    new Vector(0,0,0),
                    new Vector(0.5,0.5,0),
                    new Vector(3,3,3),
                    Quaternion.IDENTITY,
                    (byte)1
            ));
            triggerHitboxes.add(new Hitbox(
                    new Vector(0,0,0),
                    new Vector(0.5,0.5,0),
                    new Vector(1.5,3,1.5),
                    Quaternion.IDENTITY,
                    (byte)1
            ));
        }
        PlayerHandler playerHandler;
        SwordItemHandler swordItemHandler;
        public DashAttack(PlayerHandler playerHandler,SwordItemHandler itemHandler){
            this.playerHandler = playerHandler;
            this.swordItemHandler = itemHandler;
        }
        @Override
        public void run() {
            Player player = playerHandler.getPlayer();
            List<Entity> nearbyPlayers = List.copyOf(player.getLocation().getNearbyPlayers(10));
            player.setFallDistance(0);
            HashMap<Entity, Integer> triggered = HitboxCollisionUtil.getCollidedWith(
                    player.getEyeLocation(),
                    triggerHitboxes,
                    nearbyPlayers,
                    10,10,10
            );
            boolean b = !triggered.isEmpty();
            attackFrames -=1;
            if(b) {
                HashMap<Entity, Integer> collidedWith = HitboxCollisionUtil.getCollidedWith(
                        player.getEyeLocation(),
                        dashHitboxes,
                        nearbyPlayers,
                        10,10,10
                );
                for (Entity entity:collidedWith.keySet()) {
                    if(entity instanceof LivingEntity livingEntity){
                        swordItemHandler.onDamageHit(player,livingEntity,swordItemHandler);
                        Vector velocity = livingEntity.getVelocity().clone().add(new Vector(0,0.5,0));
                        livingEntity.setVelocity(velocity.add(player.getVelocity().normalize()));
                    }
                }
                player.setVelocity(new Vector(0, 0.2, 0)
                        .add(player.getEyeLocation().getDirection().multiply(-0.5)));
                swordItemHandler.playSwordAnimation(player);
                attackFrames = 0;
                playerHandler.scheduleAction(this,2);
            }else {
                if(attackFrames>0){
                    playerHandler.scheduleAction(this,1);
                }else {
                    playerHandler.cancelNextFallDamage();
                    player.setInvulnerable(false);
                }
            }
        }
    }
    @Override
    public void activateSuperchargedAbility(PlayerHandler player) {

    }
}
