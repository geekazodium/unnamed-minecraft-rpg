package com.geekazodium.cavernsofamethyst.entities.treasure;

import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.util.RandomUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.util.random.WeightedEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class RandomLootChest extends LootChestHandler{
    private final HashMap<ItemStack,Float> lootTable = new HashMap<>();
    private final Location location;
    public RandomLootChest(Location location){
        lootTable.put(new ItemStack(Material.EMERALD,1),0.1f);
        lootTable.put(CustomItemHandlerRegistry.getFromId("wooden_bow").getNewestItem(),0.005f);
        lootTable.put(CustomItemHandlerRegistry.getFromId("warm_blade").getNewestItem(),0.005f);
        lootTable.put(CustomItemHandlerRegistry.getFromId("icicle").getNewestItem(),0.005f);
        this.location = location;
        baseRespawnDuration = 1000;
        respawnVariation = 200;
    }

    @Override
    public void interact(Player player) {
        super.interact(player);
        Inventory inventory = Bukkit.createInventory(null, 27, Component.empty());
        for (int i = 0; i<27;i++) {
            ItemStack itemStack = RandomUtil.randomPickFromWeighted(lootTable, random);
            if(itemStack == null){
                continue;
            }
            inventory.setItem(i, itemStack.clone());
        }
        //inventory.setItem(13,newestItem);
        player.openInventory(inventory);
    }

    @Override
    public Location location() {
        return location;
    }
}
