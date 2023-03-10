package com.geekazodium.unnamedminecraftrpg.items.weapons.bows;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.hitbox.CollisionUtil;
import com.geekazodium.unnamedminecraftrpg.items.weapons.WeaponItemHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static com.geekazodium.unnamedminecraftrpg.util.EntityDamageUtil.copyPlayerWeaponDataToProjectile;
import static java.lang.Math.toRadians;

public abstract class BowItemHandler extends WeaponItemHandler {
    //public static final NamespacedKey damageableEntities = new NamespacedKey(Main.getInstance(),"damageableEntities");
    protected BowItemHandler(int newestVer, String id) {
        super(newestVer, id);
        actions[0]=(PlayerHandler handler)->{
            boolean b = handler.consumeMana(1,this,0);
            if(!b)return false;
            activateNormalAbility(handler);
            return true;
        };
        actions[1]=(PlayerHandler handler)->{
            boolean b = handler.consumeMana(1,this,1);
            if(!b)return false;
            activateCluster(handler);
            return true;
        };
    }

    @Override
    public void useNormalMove(Player player) {
        super.useNormalMove(player);
        playBowAnimation(player);
        Location eyeLocation = player.getEyeLocation();
        PersistentDataContainer container = player.getPersistentDataContainer();
        spawnArrow(player, eyeLocation, eyeLocation.getDirection(), container);
    }

    protected Arrow spawnArrow(Player player, Location eyeLocation, Vector direction, PersistentDataContainer container) {
        Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation, direction,3,0);
        arrow.setPersistent(false);
        arrow.setShooter(player);
        copyPlayerWeaponDataToProjectile(container,arrow,this);
        return arrow;
    }
    protected Arrow spawnArrow(Player player, Location eyeLocation, Vector direction, PersistentDataContainer container,double speed) {
        Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation, direction, (float) speed,0);
        arrow.setPersistent(false);
        arrow.setShooter(player);
        copyPlayerWeaponDataToProjectile(container,arrow,this);
        return arrow;
    }

    protected void playBowAnimation(Player player) {
        player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_ARROW_SHOOT,1,1);
    }

    public void activateNormalAbility(PlayerHandler player) {
        //player.increaseElementalCharge(1);
        player.scheduleAction(new normalAbility(this,player,player.getPlayer().getPersistentDataContainer()),1);
    }
    public void activateCluster(PlayerHandler player) {
        //player.increaseElementalCharge(1);
        player.scheduleAction(new Cluster(this,player,player.getPlayer().getPersistentDataContainer()),0);
    }
    private static class normalAbility implements Runnable{
        BowItemHandler handler;
        PlayerHandler player;
        PersistentDataContainer container;
        int rep = 10;

        public normalAbility(BowItemHandler handler, PlayerHandler player, @NotNull PersistentDataContainer container){
            this.handler = handler;
            this.player = player;
            this.container = container;
        }

        @Override
        public void run() {
            Player p = player.getPlayer();
//            Location eyeLocation = p.getEyeLocation();
//            Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation,eyeLocation.getDirection(),4,2);
//            arrow.setPersistent(false);
//            arrow.setShooter(p);
//            copyPlayerWeaponDataToProjectile(container, arrow,handler);
            handler.useNormalMove(p);
            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT, 2,1.5f);
            rep -= 1;
            if(rep>0){
                player.scheduleAction(this,2);
            }
        }
    }
    private static class Cluster implements Runnable, Listener {
        BowItemHandler handler;
        PlayerHandler player;
        PersistentDataContainer container;

        Arrow initial;

        public Cluster(BowItemHandler handler, PlayerHandler player, @NotNull PersistentDataContainer container) {
            this.handler = handler;
            this.player = player;
            this.container = container;
        }

        @Override
        public void run() {
            Player p = player.getPlayer();
            initial = handler.spawnArrow(p, p.getEyeLocation(), p.getEyeLocation().getDirection(), this.container);
            JavaPlugin instance = Main.getInstance();
            instance.getServer().getPluginManager().registerEvents(this, instance);
        }

        @EventHandler
        public void onCollide(ProjectileCollideEvent event) {
            if (event.getEntity() != this.initial) return;
            if(event.getCollidedWith() == this.player.getPlayer()){
                event.setCancelled(true);
                return;
            }
            spawnCluster(event.getEntity().getLocation());
            event.getHandlers().unregister(this);
        }

        @EventHandler
        public void onLand(ProjectileHitEvent event){
            if (event.getEntity() != this.initial) return;
            if (event.getHitBlock() == null)return;
            spawnCluster(event.getEntity().getLocation());
            HandlerList.unregisterAll(this);
        }

        private void spawnCluster(Location location){
            int count = 25;
            Player p = player.getPlayer();
            for (int i = 0; i < count; i++) {
                float y = (float) (Math.PI * 2 / ((float) count) * i);
                Quaternion quaternion = Quaternion.ONE.copy();
                quaternion.mul(Quaternion.fromYXZ(y, -0.4f, 0));
                Vector vector = CollisionUtil.applyRotationMatrix(new Vector(0, 0, 1), CollisionUtil.getRotationMatrix(quaternion));
                initial = handler.spawnArrow(p, location, vector, this.container, 0.4);
            }
        }
    }
}
