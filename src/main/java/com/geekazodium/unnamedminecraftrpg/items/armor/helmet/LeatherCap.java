package com.geekazodium.unnamedminecraftrpg.items.armor.helmet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LeatherCap extends AbstractHelmetItem {
    public LeatherCap() {
        super(1, "leather_cap");
    }

    @Override
    protected ItemStack item() {
        return new ItemStack(Material.LEATHER_HELMET);
    }

    @Override
    public int baseHealth(Player player) {
        return 1;
    }
}
