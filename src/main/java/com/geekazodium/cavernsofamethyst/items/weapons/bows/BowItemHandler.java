package com.geekazodium.cavernsofamethyst.items.weapons.bows;

import com.geekazodium.cavernsofamethyst.items.weapons.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static com.geekazodium.cavernsofamethyst.util.EntityDamageUtil.copyPlayerWeaponDataToProjectile;

public abstract class BowItemHandler extends WeaponItemHandler {
    //public static final NamespacedKey damageableEntities = new NamespacedKey(Main.getInstance(),"damageableEntities");
    protected BowItemHandler(int newestVer, String id) {
        super(newestVer, id);
        actions[0]=(PlayerHandler handler)->{
            boolean b = handler.consumeMana(1,this,0);
            if(!b)return false;
            activateNormalAbility(handler);
            return true;
        };
    }

    @Override
    public void useNormalMove(Player player) {
        super.useNormalMove(player);
        playBowAnimation(player);
        Location eyeLocation = player.getEyeLocation();
        PersistentDataContainer container = player.getPersistentDataContainer();
        spawnArrow(player, eyeLocation, eyeLocation.getDirection(), container);
    }

    protected void spawnArrow(Player player, Location eyeLocation, Vector direction, PersistentDataContainer container) {
        Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation, direction,3,0);
        arrow.setPersistent(false);
        arrow.setShooter(player);
        copyPlayerWeaponDataToProjectile(container,arrow,this);
    }

    protected void playBowAnimation(Player player) {
        player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_ARROW_SHOOT,1,1);
    }

    public void activateNormalAbility(PlayerHandler player) {
        //player.increaseElementalCharge(1);
        player.scheduleAction(new normalAbility(this,player,player.getPlayer().getPersistentDataContainer()),1);
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
//            Location eyeLocation = p.getEyeLocation();
//            Arrow arrow = eyeLocation.getWorld().spawnArrow(eyeLocation,eyeLocation.getDirection(),4,2);
//            arrow.setPersistent(false);
//            arrow.setShooter(p);
//            copyPlayerWeaponDataToProjectile(container, arrow,handler);
            handler.useNormalMove(p);
            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT, 2,1.5f);
            rep -= 1;
            if(rep>0){
                player.scheduleAction(this,2);
            }
        }
    }
}
