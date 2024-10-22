package com.geekazodium.unnamedminecraftrpg.entities.holograms;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

import static com.geekazodium.unnamedminecraftrpg.entities.mobs.MobBehavior.MOB_DISPLAY_NAME_KEY;
import static com.geekazodium.unnamedminecraftrpg.util.EntityDisplayUtil.formatName;

public class EntityNameHologram extends HologramHandler{
    public EntityNameHologram(LivingEntity entity){
        String name = entity.getPersistentDataContainer().get(MOB_DISPLAY_NAME_KEY, PersistentDataType.STRING);
        if(name == null){name = "null";}
        spawn(formatName(name),entity.getLocation());
    }
}
