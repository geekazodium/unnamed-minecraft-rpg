package com.geekazodium.cavernsofamethyst.items.Swords;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class WarmBlade extends SwordItemHandler {
    public WarmBlade() {
        super(0,"warm_blade");
    }

    @Override
    protected ItemStack item() {
        ItemStack itemStack = new ItemStack(Material.STONE_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*@Override
    public int baseAttack(Player player) {
        return 10;
    }*/

    @Override
    public int fireBaseDamage() {
        return 5;
    }
}
