package com.geekazodium.cavernsofamethyst.entities.mobs;

import com.geekazodium.cavernsofamethyst.entities.mobs.pathfinder.IdleLimitedMobGoal;
import com.geekazodium.cavernsofamethyst.entities.mobs.pathfinder.TargetLimitedMobGoal;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static com.geekazodium.cavernsofamethyst.listeners.EntityDeathListener.ENTITY_XP_KEY;
import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.FIRE_BASE_DAMAGE;
import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.NEUTRAL_BASE_DAMAGE;

public class BobSpawnMobBehavior extends MobBehavior{
    public BobSpawnMobBehavior(Location center, int entityCap, double spawnRadius) {
        super(center, entityCap, spawnRadius);
        spawnDelay=1000;
        spawnTick=1000;
    }

    @Override
    protected @NotNull LivingEntity spawnEntity(World world, Location location) {
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.IRON_GOLEM,false);
        DisguiseAPI.disguiseEntity(entity, new MobDisguise(DisguiseType.WITHER));
        setMobDisplayNameKey(entity,"BOB");
        entity.setCustomNameVisible(true);
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert attribute != null;
        int health = 2048;
        AttributeInstance armorAttribute = entity.getAttribute(Attribute.GENERIC_ARMOR);
        assert armorAttribute != null;
        armorAttribute.setBaseValue(1000);
        attribute.setBaseValue(health);
        entity.setHealth(health);
        Bukkit.getMobGoals().removeAllGoals((Mob) entity);
        Bukkit.getMobGoals().addGoal((Mob) entity,1,
                new IdleLimitedMobGoal((Mob) entity,location,4));
        Bukkit.getMobGoals().addGoal((Mob) entity,0,
                new TargetLimitedMobGoal((Mob) entity,location,100));
        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(NEUTRAL_BASE_DAMAGE, PersistentDataType.INTEGER,999999);
        persistentDataContainer.set(ENTITY_XP_KEY,PersistentDataType.INTEGER,200000);
        return entity;
    }
}
