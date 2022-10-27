package com.geekazodium.cavernsofamethyst.entities.mobs;

import com.destroystokyo.paper.entity.ai.PaperVanillaGoal;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static com.geekazodium.cavernsofamethyst.listeners.EntityDeathListener.ENTITY_XP_KEY;
import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.*;

public class ZombieSpawnMobBehavior extends MobBehavior{
    public ZombieSpawnMobBehavior(Location center, int entityCap, double spawnRadius) {
        super(center, entityCap, spawnRadius);
    }

    @Override
    protected @NotNull LivingEntity spawnEntity(World world, Location location) {
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.HUSK,false);
        DisguiseAPI.disguiseEntity(entity, new MobDisguise(DisguiseType.ZOMBIE));
        setMobDisplayNameKey(entity,"zombie");
        entity.setCustomNameVisible(true);
        //Bukkit.getLogger().warning(Arrays.toString(Bukkit.getMobGoals().getAllGoals((Mob) entity).toArray()));
        Bukkit.getMobGoals().removeGoal((Creature) entity, PaperVanillaGoal.WATER_AVOIDING_RANDOM_STROLL);
        Bukkit.getMobGoals().removeGoal((Creature) entity, PaperVanillaGoal.RANDOM_STROLL);
        Bukkit.getMobGoals().addGoal((Mob) entity,10,new AreaLimitMobGoal((Mob) entity,location,4));
        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(WATER_BASE_DAMAGE, PersistentDataType.INTEGER,2);
        persistentDataContainer.set(FIRE_BASE_DAMAGE, PersistentDataType.INTEGER,2);
        persistentDataContainer.set(EARTH_BASE_DAMAGE, PersistentDataType.INTEGER,2);
        persistentDataContainer.set(ENTITY_XP_KEY,PersistentDataType.INTEGER,2);
        return entity;
    }
}
