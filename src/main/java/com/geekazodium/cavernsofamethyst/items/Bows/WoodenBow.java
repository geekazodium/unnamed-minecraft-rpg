package com.geekazodium.cavernsofamethyst.items.Bows;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.*;

public class WoodenBow extends BowItemHandler {
    public WoodenBow() {
        super(0,"wooden_bow");
    }

    @Override
    protected ItemStack item() {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
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
