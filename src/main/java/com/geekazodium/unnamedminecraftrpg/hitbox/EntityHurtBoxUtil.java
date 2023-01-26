package com.geekazodium.unnamedminecraftrpg.hitbox;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

public class EntityHurtBoxUtil{
    private static final HashMap<Entity,Hitbox> entityHitboxCache = new HashMap<>();

    public static Hitbox getForEntity(Entity entity){
        if(entityHitboxCache.containsKey(entity)){
            return entityHitboxCache.get(entity);
        }
        Hitbox hitbox = Hitbox.fromBoundingBox(entity.getBoundingBox());
        entityHitboxCache.put(entity,hitbox);
        return hitbox;
    }

    public static void remove(LivingEntity entity) {
        entityHitboxCache.remove(entity);
    }
}
