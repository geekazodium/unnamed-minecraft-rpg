package com.geekazodium.unnamedminecraftrpg.entities.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface Hologram {
    static Object spawnForDamageEvent(Entity damager, LivingEntity entity, int fire, int earth, int water, int neutral, int element) {
        return new DamageHologram(damager,entity,fire,earth,water,neutral,element);
    }

    boolean removed();
    static Hologram spawnForEntity(LivingEntity entity){
        return new EntityNameHologram(entity);
    }
    void remove();

    void teleport(Location location);
}
