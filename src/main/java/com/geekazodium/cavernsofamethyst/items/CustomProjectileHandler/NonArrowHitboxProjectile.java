package com.geekazodium.cavernsofamethyst.items.CustomProjectileHandler;

import com.geekazodium.cavernsofamethyst.hitbox.Hitbox;
import com.geekazodium.cavernsofamethyst.hitbox.HitboxCollisionUtil;
import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import com.geekazodium.cavernsofamethyst.util.Quaternion;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NonArrowHitboxProjectile implements Projectile{
    protected LinkedList<Hitbox> hitboxes = new LinkedList<>();
    protected Vector gravity = new Vector(0,-0.01,0);
    //private final PersistentDataContainer weaponData;
    protected Entity shooter;
    private final ArmorStand arrow;
    protected final Location location;
    protected Vector velocity;
    public NonArrowHitboxProjectile(Entity shooter, Location initial, Vector velocity, WeaponItemHandler handler){
        hitboxes.add(new Hitbox(
                new Vector(0,0,0),
                new Vector(0.5,0.5,0),
                new Vector(0,0,0),
                Quaternion.IDENTITY,
                (byte)3
        ));
        location = initial;
        this.shooter = shooter;
        arrow = (ArmorStand) shooter.getWorld().spawnEntity(initial, EntityType.ARMOR_STAND);
        EntityDamageUtil.copyPlayerWeaponDataToProjectile(
                shooter.getPersistentDataContainer(),
                arrow,
                handler
        );
        //DisguiseAPI.disguiseEntity(arrow,new MiscDisguise(DisguiseType.ARROW));
        this.velocity = velocity;
        //arrow.setVelocity(velocity);
        arrow.setPersistent(false);
        arrow.setInvulnerable(true);
        //arrow.setNoPhysics(true);
        arrow.setCollidable(false);
        arrow.setGravity(false);
    }

    @Override
    public void tick() {
        location.add(velocity);
        location.setPitch((float) -Math.toDegrees(Math.asin(velocity.getY()/velocity.length())));
        arrow.teleport(location);
        velocity.add(gravity);
        hitboxes.get(0).size = new Vector(0.5,0.5,velocity.length());
        HashMap<Entity, Integer> collidedWith = HitboxCollisionUtil.getCollidedWith(
                arrow.getLocation(),
                hitboxes,
                List.of(shooter,arrow),
                velocity.getX() * 2, velocity.getY() * 2, velocity.getZ() * 2
        );
        double closest = velocity.length()+1;
        LivingEntity closestDamageable = null;
        for (Entity entity : collidedWith.keySet()) {
            if(shooter instanceof Player) {
                if (entity instanceof Player){
                    continue;
                }
                if (entity instanceof LivingEntity livingEntity) {
                    double d = livingEntity.getLocation().distance(arrow.getLocation());
                    if(d<closest){
                        closest=d;
                        closestDamageable = livingEntity;
                    }
                }
            }
        }
        if(closestDamageable !=null){
            if(shooter instanceof Player player) {
                onArrowHit(true,player, getWeaponStats(), closestDamageable);
            }
        }
        if(arrow.rayTraceBlocks(velocity.length()) != null){
            if(shooter instanceof Player player) {
                onArrowHit(false,player, getWeaponStats(), null);
            }
        }
    }

    public void onArrowHit(boolean hitEntity,Player player, PersistentDataContainer weaponStats, LivingEntity entityHit){
        arrow.remove();
    }

    @Override
    public Entity getProjectile() {
        return arrow;
    }

    @Override
    public PersistentDataContainer getWeaponStats() {
        return arrow.getPersistentDataContainer();
    }

    // @Override
    //public PersistentDataContainer getWeaponStats() {
        //return weaponData;
   // }

    @Override
    public Entity getShooter() {
        return shooter;
    }

    @Override
    public boolean removed() {
        return arrow.isDead();
    }
}
