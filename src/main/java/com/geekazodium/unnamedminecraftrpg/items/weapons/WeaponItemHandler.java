package com.geekazodium.unnamedminecraftrpg.items.weapons;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.items.CustomItemHandler;
import com.geekazodium.unnamedminecraftrpg.util.EntityDamageUtil;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static com.geekazodium.unnamedminecraftrpg.GameTickHandler.getPlayerHandler;

//import static com.geekazodium.cavernsofamethyst.GameTickHandler.playersAttackCooldown;

public abstract class WeaponItemHandler extends CustomItemHandler {
    //public static final NamespacedKey PLAYER_ELEMENTAL_CHARGE = new NamespacedKey(Main.getInstance(),"elemental_charge");
    protected int attackDelayTier = 4;

    protected final SpecialAction[] actions = new SpecialAction[7];

    public SpecialAction getSpecialAction(int id){
        return actions[id];
    }

    protected WeaponItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }
    protected void onDamageHit(Player player, LivingEntity entity,WeaponItemHandler itemHandler) {
        EntityDamageUtil.onPlayerDamageEntity(player,entity,itemHandler);
    }

    @Override
    public final void onLeftClickMainHand(PlayerArmSwingEvent event) {
        useNormalMove(event.getPlayer());
    }

    @Override
    public final void onRightClickMainHand(PlayerInteractEvent event) {
    }

    public void setPlayerAttackCooldown(Player player){
        int delay = Math.max(1,Math.min(attackDelayTier,8));
        delay *= 5;
        getPlayerHandler(player).setAtkCooldown(delay);
        player.setCooldown(player.getInventory().getItemInMainHand().getType(),
                Math.max(0, getPlayerHandler(player).getAtkCooldown())
        );
    }
    //public abstract void activateNormalAbility(PlayerHandler player);

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

    public void useNormalMove(Player player){
        setPlayerAttackCooldown(player);
    }
    public void useSpecialAction(Player player,int i) {
        SpecialAction action = actions[i];
        if(action == null) return;
        action.use(GameTickHandler.getPlayerHandler(player));
    }

    @FunctionalInterface
    public interface SpecialAction{
        boolean use(PlayerHandler playerHandler);
    }
}
