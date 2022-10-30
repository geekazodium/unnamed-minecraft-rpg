package com.geekazodium.cavernsofamethyst.items.projectiles;

import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;

public interface Projectile {
    void tick();
    Entity getProjectile();
    PersistentDataContainer getWeaponStats();
    Entity getShooter();
    boolean removed();
}
