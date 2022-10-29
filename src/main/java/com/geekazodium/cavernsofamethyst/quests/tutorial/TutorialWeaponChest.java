package com.geekazodium.cavernsofamethyst.quests.tutorial;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.entities.treasure.LootChestHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.util.RandomUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TutorialWeaponChest extends LootChestHandler {
    public TutorialWeaponChest(){
        respawnVariation = 600;
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
