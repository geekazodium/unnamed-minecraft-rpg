package com.geekazodium.cavernsofamethyst.items.weapons.bows;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WoodenBow extends BowItemHandler {
    public WoodenBow() {
        super(0,"wooden_bow");
    }

    @Override
    protected ItemStack item() {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*
        @Override
        public int baseMana(Player player) {
            return 10;
        }*/
    @Override
    public int earthBaseDamage() {
        return 5;
    }
}
