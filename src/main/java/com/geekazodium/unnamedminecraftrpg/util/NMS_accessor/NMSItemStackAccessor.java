package com.geekazodium.unnamedminecraftrpg.util.NMS_accessor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;

/**
 * This is a dummy class used to access net.minecraft.world.item.ItemStack
 * this exists because the class name overlapped with the bukkit ItemStack
 */
public class NMSItemStackAccessor {
    private final ItemStack itemStack;
    public NMSItemStackAccessor(ItemLike item) {
        itemStack = new ItemStack(item);
    }

    public NMSItemStackAccessor(Holder<Item> entry) {
        itemStack = new ItemStack(entry);
    }

    public NMSItemStackAccessor(ItemLike item, int count) {
        itemStack = new ItemStack(item,count);
    }

    private NMSItemStackAccessor(ItemStack itemStack){
        this.itemStack = itemStack;
    }
    public NMSItemStackAccessor(){//creates empty stack
        this.itemStack = new ItemStack(Items.AIR);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static NMSItemStackAccessor fromItemStack(ItemStack itemStack){
        return new NMSItemStackAccessor(itemStack);
    }
}
