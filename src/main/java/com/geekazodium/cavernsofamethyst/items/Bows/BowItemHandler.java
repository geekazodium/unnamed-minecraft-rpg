package com.geekazodium.cavernsofamethyst.items.Bows;

import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import static com.geekazodium.cavernsofamethyst.util.EntityDamageUtil.copyPlayerWeaponDataToProjectile;

public abstract class BowItemHandler extends WeaponItemHandler {
    //public static final NamespacedKey damageableEntities = new NamespacedKey(Main.getInstance(),"damageableEntities");
    protected BowItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }

    @Override
    public void onLeftClickMainHand(PlayerArmSwingEvent event) {
        super.onLeftClickMainHand(event);
        Player player = event.getPlayer();
        playBowAnimation(player);
        Location eyeLocation = player.getEyeLocation();
        PersistentDataContainer container = player.getPersistentDataContainer();
        Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation,eyeLocation.getDirection(),3,0);
        arrow.setPersistent(false);
        arrow.setShooter(player);
        copyPlayerWeaponDataToProjectile(container,arrow,this);
        //NonArrowHitboxProjectile projectile = new NonArrowHitboxProjectile(player, eyeLocation, eyeLocation.getDirection().multiply(3),container);
        //GameTickHandler.getInstance().overworldProjectileHandler.addTickingProjectile(projectile);
    }

    protected void playBowAnimation(Player player) {
    }
}
