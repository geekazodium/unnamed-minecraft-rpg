package com.geekazodium.cavernsofamethyst.items.Wands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.WATER_BASE_DAMAGE;

public class Icicle extends WandItemHandler{
    public Icicle(){
        super(0,"icicle");
    }

    @Override
    protected ItemStack item() {
        ItemStack itemStack = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*@Override
    public int baseHealth(Player player) {
        return 10;
    }*/

    @Override
    public int waterBaseDamage() {
        return 5;
    }
}
