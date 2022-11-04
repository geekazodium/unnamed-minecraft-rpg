package com.geekazodium.cavernsofamethyst.items.weapons;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static com.geekazodium.cavernsofamethyst.GameTickHandler.getPlayerHandler;

//import static com.geekazodium.cavernsofamethyst.GameTickHandler.playersAttackCooldown;

public abstract class WeaponItemHandler extends CustomItemHandler {
    //public static final NamespacedKey PLAYER_ELEMENTAL_CHARGE = new NamespacedKey(Main.getInstance(),"elemental_charge");
    protected int attackDelayTier = 4;

    protected WeaponItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }
    protected void onDamageHit(Player player, LivingEntity entity,WeaponItemHandler itemHandler) {
        EntityDamageUtil.onPlayerDamageEntity(player,entity,itemHandler);
    }

    @Override
    public void onLeftClickMainHand(PlayerArmSwingEvent event) {
        Player player = event.getPlayer();
        setPlayerAttackCooldown(player);
    }

    @Override
    public void onRightClickMainHand(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        //@NotNull ItemStack item = player.getInventory().getItemInMainHand();
        PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(player);
        if(playerHandler.elementalCharge() >= elementalChargeReq()){
            activateSuperchargedAbility(playerHandler);
            playerHandler.consumeElementalCharge(elementalChargeReq());
        }else{
            activateNormalAbility(playerHandler);
        }
    }

    public void setPlayerAttackCooldown(Player player){
        int delay = Math.max(1,Math.min(attackDelayTier,8));
        delay *= 5;
        getPlayerHandler(player).setAtkCooldown(delay);
        player.setCooldown(player.getInventory().getItemInMainHand().getType(),
                Math.max(0, getPlayerHandler(player).getAtkCooldown())
        );
    }
    public abstract void activateNormalAbility(PlayerHandler player);

    public abstract void activateSuperchargedAbility(PlayerHandler player);

    public int fireBaseDamage(){
        return 0;
    }

    public int earthBaseDamage(){
        return 0;
    }

    public int waterBaseDamage(){
        return 0;
    }

    public int neutralBaseDamage() {return 0;}

    @Override
    protected EquipmentSlot effectiveSlot() {
        return EquipmentSlot.HAND;
    }

    public int elementalChargeReq(){return Integer.MAX_VALUE;}
}
