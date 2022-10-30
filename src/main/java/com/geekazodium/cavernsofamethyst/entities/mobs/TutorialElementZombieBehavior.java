package com.geekazodium.cavernsofamethyst.entities.mobs;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.*;

public class TutorialElementZombieBehavior extends ZombieSpawnMobBehavior{
    private static NamespacedKey entityElement = new NamespacedKey(Main.getInstance(),"entityElement");
    public TutorialElementZombieBehavior(Location center, int entityCap, double spawnRadius) {
        super(center, entityCap, spawnRadius);
    }

    @Override
    protected @NotNull LivingEntity spawnEntity(World world, Location location) {
        LivingEntity entity = super.spawnEntity(world, location);
        int value = this.random.nextInt(0, 3);
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(entityElement,PersistentDataType.INTEGER, value);
        container.set(NEUTRAL_BASE_DAMAGE, PersistentDataType.INTEGER,0);
        if(value == 0){
            container.set(FIRE_BASE_DAMAGE, PersistentDataType.INTEGER,1);
        }else if(value == 1){
            container.set(EARTH_BASE_DAMAGE, PersistentDataType.INTEGER,1);
        } else if (value == 2) {
            container.set(WATER_BASE_DAMAGE, PersistentDataType.INTEGER,1);
        }
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
