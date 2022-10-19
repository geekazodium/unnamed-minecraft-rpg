package com.geekazodium.cavernsofamethyst.hitbox;

import com.geekazodium.cavernsofamethyst.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class HitboxCollisionUtil {
    public static HashMap<Entity,Integer> getCollidedWith(Location location, LinkedList<Hitbox> hitboxes, List<Entity> exclude, double x, double y, double z){
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, x, y, z);
        entities.removeAll(exclude);
        for (Hitbox hitbox:hitboxes){
            hitbox.updateCollider(location);
            if(Main.debugHitbox){
                for (Vector vector:hitbox.getOutline()) {
                    Location location1 = new Location(location.getWorld(), vector.getX(), vector.getY(), vector.getZ());
                    for (Player player:location1.getWorld().getPlayers()) {
                        player.spawnParticle(Particle.REDSTONE, location1,1, new Particle.DustOptions(Color.RED,1));
                    }
                }
            }
        }
        HashMap<Entity,Integer> collided = new HashMap<>();
        for (Entity entity:entities){
            if(entity.isInvulnerable()||entity.isDead()){
                continue;
            }
            Hitbox entityHitbox = EntityHurtBoxUtil.getForEntity(entity);
            entityHitbox.updateCollider(entity.getBoundingBox().getCenter(),0,0);
            if(Main.debugHitbox){
                for (Vector vector:entityHitbox.getOutline()) {
                    Location location1 = new Location(location.getWorld(), vector.getX(), vector.getY(), vector.getZ());
                    for (Player player:location1.getWorld().getPlayers()) {
                        player.spawnParticle(Particle.REDSTONE, location1,1, new Particle.DustOptions(Color.GREEN,1));
                    }
                }
            }
            int c = 0;
            for (Hitbox hitbox: hitboxes){
                if(hitbox.isColliding(entityHitbox)){
                    collided.put(entity,c);
                    break;
                }
                c+=1;
            }
        }//TODO remember to stress test this algorithm
        return collided;
    }

    public static HashMap<Entity,Integer> getCollidedWith(Location location, LinkedList<Hitbox> hitboxes, List<Entity> exclude) {
        return getCollidedWith(location,hitboxes,exclude,20,20,20);
    }

    public static HashMap<Entity,Integer> getCollidedWith(Location location, LinkedList<Hitbox> hitboxes) {
        return getCollidedWith(location,hitboxes,new ArrayList<>());
    }
}
