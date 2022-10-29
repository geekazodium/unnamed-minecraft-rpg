package com.geekazodium.cavernsofamethyst.items.Wands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class Icicle extends WandItemHandler{
    public Icicle(){
        super(0,"icicle");
    }

    @Override
    protected ItemStack item() {
        ItemStack itemStack = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta itemMeta = itemStack.getItemMeta();
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
