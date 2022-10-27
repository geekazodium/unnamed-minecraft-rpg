package com.geekazodium.cavernsofamethyst.util;

import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

public class RandomUtil {
    public static <T> T randomPickFromList(List<T> list, Random random){
        return list.get(random.nextInt(list.size()));
    }

    public static Inventory generateChestLoot(){//TODO add loot tables and stuff
        return null;
    }
}
