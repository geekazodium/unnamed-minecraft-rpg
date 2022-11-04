package com.geekazodium.cavernsofamethyst.items;

import com.geekazodium.cavernsofamethyst.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public abstract class UnstackableCustomItem extends CustomItemHandler{
    private final NamespacedKey unstackableKey = new NamespacedKey(Main.getInstance(),"unstackable_unique_data");
    protected UnstackableCustomItem(int newestVer, String id) {
        super(newestVer, id);
    }

    @Override
    public ItemStack getNewestItem() {
        ItemStack newestItem = super.getNewestItem();
        ItemMeta itemMeta = newestItem.getItemMeta();
        itemMeta.getPersistentDataContainer().set(
                unstackableKey,
                PersistentDataType.STRING,
                newestItem.hashCode() +
                        UUID.randomUUID().toString() +
                        System.currentTimeMillis()
        );
        newestItem.setItemMeta(itemMeta);
        return newestItem;
    }
}
