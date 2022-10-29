package com.geekazodium.cavernsofamethyst.items.Bows;

import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.util.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

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
        player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_ARROW_SHOOT,1,1);
    }

    @Override
    public void activateNormalAbility(PlayerHandler player) {
        if(!player.consumeMana(1)){
            return;
        }
        //player.increaseElementalCharge(1);
        player.scheduleAction(new normalAbility(this,player,item().getItemMeta().getPersistentDataContainer()),1);
    }
    private static class normalAbility implements Runnable{
        BowItemHandler handler;
        PlayerHandler player;
        PersistentDataContainer container;
        int rep = 10;

        public normalAbility(BowItemHandler handler, PlayerHandler player, @NotNull PersistentDataContainer container){
            this.handler = handler;
            this.player = player;
            this.container = container;
        }

        @Override
        public void run() {
            Player p = player.getPlayer();
            Location eyeLocation = p.getEyeLocation();
            Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation,eyeLocation.getDirection(),4,2);
            arrow.setPersistent(false);
            arrow.setShooter(p);
            copyPlayerWeaponDataToProjectile(container, arrow,handler);
            rep -= 1;
            if(rep>0){
                player.scheduleAction(this,2);
            }
        }
    }
    @Override
    public void activateSuperchargedAbility(PlayerHandler player) {}
}
