package com.geekazodium.cavernsofamethyst.items.CustomProjectileHandler;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ProjectileHandler {
    World world;
    List<Projectile> projectiles = new ArrayList<>();

    public ProjectileHandler(World world){
        this.world = world;
    }
    public void tick(){
        projectiles.forEach(Projectile::tick);
        projectiles.removeIf(Projectile::removed);
    }

    public void addTickingProjectile(NonArrowHitboxProjectile projectile) {
        projectiles.add(projectile);
    }
}
