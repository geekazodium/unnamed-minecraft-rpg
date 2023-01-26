package com.geekazodium.unnamedminecraftrpg.entities.mobs;

import com.geekazodium.unnamedminecraftrpg.entities.mobs.pathfinder.IdleLimitedMobGoal;
import com.geekazodium.unnamedminecraftrpg.entities.mobs.pathfinder.TargetLimitedMobGoal;
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

import static com.geekazodium.unnamedminecraftrpg.listeners.EntityDeathListener.ENTITY_XP_KEY;
import static com.geekazodium.unnamedminecraftrpg.util.ElementalReactionUtil.*;

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
        Bukkit.getMobGoals().removeAllGoals((Mob) entity);
        Bukkit.getMobGoals().addGoal((Mob) entity,1,
                new IdleLimitedMobGoal((Mob) entity,location,4));
        Bukkit.getMobGoals().addGoal((Mob) entity,0,
                new TargetLimitedMobGoal((Mob) entity,location,10));
        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(NEUTRAL_BASE_DAMAGE, PersistentDataType.INTEGER,1);
        persistentDataContainer.set(ENTITY_XP_KEY,PersistentDataType.INTEGER,2);
        return entity;
    }
}
