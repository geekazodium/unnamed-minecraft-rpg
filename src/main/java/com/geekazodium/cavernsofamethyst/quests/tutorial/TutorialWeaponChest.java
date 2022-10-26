package com.geekazodium.cavernsofamethyst.quests.tutorial;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.entities.treasure.LootChestHandler;
import com.geekazodium.cavernsofamethyst.items.Bows.WoodenBow;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.items.Wands.Icicle;
import com.geekazodium.cavernsofamethyst.util.RandomUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.random.RandomGenerator;

public class TutorialWeaponChest extends LootChestHandler {
    public TutorialWeaponChest(){
        respawnVariation = 1200;
        baseRespawnDuration = 400;
    }
    @Override
    public Location location() {
        return new Location(Main.overworld,-144,77,16);
    }

    @Override
    public void interact(Player player) {
        super.interact(player);
        Inventory inventory = Bukkit.createInventory(null, 27, Component.empty());
        ItemStack newestItem = RandomUtil.randomPickFromList(
                List.of(
                        CustomItemHandlerRegistry.getFromId("wooden_bow"),
                        CustomItemHandlerRegistry.getFromId("icicle"),
                        CustomItemHandlerRegistry.getFromId("warm_blade")
                ),
                random
        ).getNewestItem();
        inventory.setItem(13,newestItem);
        player.openInventory(inventory);
    }
}
