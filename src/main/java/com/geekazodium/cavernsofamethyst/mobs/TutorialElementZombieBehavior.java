package com.geekazodium.cavernsofamethyst.mobs;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.naming.Name;

public class TutorialElementZombieBehavior extends ZombieSpawnMobBehavior{
    private static NamespacedKey entityElement = new NamespacedKey(Main.getInstance(),"entityElement");
    public TutorialElementZombieBehavior(Location center, int entityCap, double spawnRadius) {
        super(center, entityCap, spawnRadius);
    }

    @Override
    protected @NotNull LivingEntity spawnEntity(World world, Location location) {
        LivingEntity entity = super.spawnEntity(world, location);
        entity.getPersistentDataContainer().set(entityElement,PersistentDataType.INTEGER,this.random.nextInt(0,3));
        return entity;
    }

    @Override
    public void tick(World world) {
        super.tick(world);
        entities.forEach(entity -> ElementalReactionUtil.applyElement(
                entity,
                entity.getPersistentDataContainer().getOrDefault(
                        entityElement,
                        PersistentDataType.INTEGER,
                        0
                ),
                10,
                10
        ));
    }
}
