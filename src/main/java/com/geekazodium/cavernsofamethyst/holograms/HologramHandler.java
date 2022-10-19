package com.geekazodium.cavernsofamethyst.holograms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class HologramHandler implements Hologram{
    private boolean removed = false;
    protected ArmorStand armorStand;
    protected void spawn(Component displayName, Location location){
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCollidable(false);
        armorStand.setCustomNameVisible(true);
        armorStand.customName(displayName);
    }

    public void remove(){
        armorStand.remove();
        this.armorStand = null;
        removed = true;
    }

    @Override
    public void teleport(Location location) {
        if(armorStand == null){
            return;
        }
        armorStand.teleport(location);
    }

    @Override
    public boolean removed() {
        return removed;
    }
}
