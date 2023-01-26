package com.geekazodium.unnamedminecraftrpg.hitbox;

import com.geekazodium.unnamedminecraftrpg.Main;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class HitboxCollisionUtil {
    public static HashMap<Entity,Integer> getCollidedWith(Location location, LinkedList<Hitbox> hitboxes, List<Entity> exclude, double x, double y, double z){
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, x, y, z);
        entities.removeAll(exclude);
        for (Hitbox hitbox:hitboxes){
            hitbox.updateCollider(location);
            if(Main.debugHitbox){
                hitbox.renderOutline(location.getWorld());
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
                entityHitbox.renderOutline(location.getWorld());
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
