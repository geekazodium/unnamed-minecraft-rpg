package com.geekazodium.cavernsofamethyst.items.Wands;

import com.geekazodium.cavernsofamethyst.items.CustomProjectileHandler.NonArrowHitboxProjectile;
import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;

public class EnergyOrb extends NonArrowHitboxProjectile {
    public EnergyOrb(Entity shooter, Location initial, Vector velocity, WeaponItemHandler handler) {
        super(shooter, initial, velocity.multiply(2), handler);
        DisguiseAPI.disguiseEntity(this.getProjectile(),new MobDisguise(DisguiseType.SLIME));
    }

    @Override
    public void onArrowHit(boolean hitEntity, Player player, PersistentDataContainer weaponStats, LivingEntity entityHit) {
        getProjectile().teleport(location.clone().add(velocity));
        super.onArrowHit(hitEntity, player, weaponStats, entityHit);
        Location l = (entityHit == null)?location:entityHit.getBoundingBox().getCenter().toLocation(location.getWorld());
        l.getWorld().getNearbyLivingEntities(l,5).forEach(livingEntity ->
                EntityDamageUtil.onPlayerProjectileDamageEntity(player,weaponStats,livingEntity)
        );
        location.getNearbyPlayers(30).forEach(p ->{
            p.spawnParticle(Particle.EXPLOSION_HUGE,location,3);
        });
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE,2,1);
    }
}
