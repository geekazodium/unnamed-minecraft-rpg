package com.geekazodium.cavernsofamethyst.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class CustomItemHandlerRegistry {
    private static final HashMap<String, CustomItemHandler> itemHandlers = new HashMap<>();
    public static final @NotNull NamespacedKey HANDLER_ID = Objects.requireNonNull(NamespacedKey.fromString("handlerid"));
    public static final @NotNull NamespacedKey HANDLER_VERSION = Objects.requireNonNull(NamespacedKey.fromString("handlerversion"));

    public static CustomItemHandler get(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null){
            return null;
        }
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        String handlerId = container.getOrDefault(HANDLER_ID, PersistentDataType.STRING, "vanilla");
        return itemHandlers.get(handlerId);
    }

    public static void register(String id, CustomItemHandler customItemHandler){
        itemHandlers.put(id,customItemHandler);
    }

    public static CustomItemHandler getFromId(String id) {
        return itemHandlers.get(id);
    }

    public static String getIdForHandler(WeaponItemHandler itemHandler) {
        return itemHandler.id;
    }
}
