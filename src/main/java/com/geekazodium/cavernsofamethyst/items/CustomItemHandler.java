package com.geekazodium.cavernsofamethyst.items;

import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry.HANDLER_ID;
import static com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry.HANDLER_VERSION;

public abstract class CustomItemHandler {
    protected final int newestVer;
    protected final String id;
    int itemRarity;

    protected CustomItemHandler(int newestVer, String id) {
        this.newestVer = newestVer;
        this.id = id;
    }

    public void checkIfItemUpdated(Inventory inventory,int index, PersistentDataContainer container){
        int handlerVersion = container.getOrDefault(HANDLER_VERSION, PersistentDataType.INTEGER, 0);
        if(handlerVersion<newestVer){
            inventory.setItem(index,getNewestItem());
        }
    }
    public void register(){
        CustomItemHandlerRegistry.register(id,this);
    }

    public void onLeftClickOffHand(PlayerArmSwingEvent event){}
    public void onLeftClickMainHand(PlayerArmSwingEvent event) {}
    public void onRightClickOffHand(PlayerInteractEvent event){}
    public void onRightClickMainHand(PlayerInteractEvent event) {}

    protected abstract ItemStack item();

    private ItemStack addItemIdToData(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(HANDLER_ID, PersistentDataType.STRING,id);
        container.set(HANDLER_VERSION, PersistentDataType.INTEGER,newestVer);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getNewestItem() {
        return addItemIdToData(item());
    }

    public abstract boolean isPlayerOnCD(Player player);
    public int baseHealth(Player player){
        return 0;
    }
    public int baseAttack(Player player){
        return 0;
    }
    public int baseMana(Player player){
        return 0;
    }

    public void applyItemBaseStats(PlayerHandler.PlayerStats stats,Player player) {
        stats.maxHealth += this.baseHealth(player);
        stats.baseAttack += this.baseAttack(player);
        stats.maxMana += this.baseMana(player);
    }
}
