package com.geekazodium.cavernsofamethyst.items.armor.helmet;

import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.armor.AbstractArmorItemHandler;
import com.geekazodium.cavernsofamethyst.listeners.PlayerUpdateInventoryListener;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static org.bukkit.inventory.EquipmentSlot.HEAD;

public abstract class AbstractHelmetItem extends AbstractArmorItemHandler implements Helmet {
    public AbstractHelmetItem(int newestVer, String id) {
        super(newestVer, id);
    }

    @Override
    public void onRightClickMainHand(PlayerInteractEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getItem();
        if(item == null){
            return;
        }
        PlayerInventory inventory = event.getPlayer().getInventory();
        if(inventory.getItem(HEAD).getType() == Material.AIR){
            inventory.remove(item);
            inventory.setItem(HEAD, item);
        }
        super.onRightClickMainHand(event);
        PlayerUpdateInventoryListener.updatePlayer(event.getPlayer());
    }

    @Override
    protected EquipmentSlot effectiveSlot() {
        return HEAD;
    }
}
