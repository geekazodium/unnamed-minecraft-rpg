package com.geekazodium.cavernsofamethyst.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface Hologram {
    boolean removed();
    static Hologram spawnForEntity(LivingEntity entity){
        return new EntityNameHologram(entity);
    }

    static Hologram spawnForDamageEvent(Entity damager, LivingEntity entity, int fire, int earth, int water, int element) {
        return new DamageHologram(damager,entity,fire,earth,water,element);
    }

    void remove();

    void teleport(Location location);
}
