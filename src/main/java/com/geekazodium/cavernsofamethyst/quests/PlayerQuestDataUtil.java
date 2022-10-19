package com.geekazodium.cavernsofamethyst.quests;

import com.geekazodium.cavernsofamethyst.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_18_R2.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;

import javax.naming.Name;
import java.util.Arrays;

public class PlayerQuestDataUtil {
    //public static final NamespacedKey playerQuests = new NamespacedKey(Main.getInstance(),"quest");
    public static final NamespacedKey questProgress = new NamespacedKey(Main.getInstance(),"progress");
    public static final NamespacedKey questExtra = new NamespacedKey(Main.getInstance(),"extra");

    public static PersistentDataContainer getQuestData(Player player,String quest){
        ItemStack blank = new ItemStack(Material.ARROW);
        PersistentDataContainer playerPersistentData = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "quest_" + quest);
        return playerPersistentData.getOrDefault(key, PersistentDataType.TAG_CONTAINER, blank.getItemMeta().getPersistentDataContainer());
    }

    public static void updateQuestData(Player player,String quest,PersistentDataContainer container){
        PersistentDataContainer playerPersistentData = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "quest_" + quest);
        playerPersistentData.set(key,PersistentDataType.TAG_CONTAINER,container);
    }

    public static int getQuestProgress(PersistentDataContainer container){
        return container.getOrDefault(questProgress,PersistentDataType.INTEGER,0);
    }

    public static void setQuestProgress(PersistentDataContainer container, int i) {
        container.set(questProgress,PersistentDataType.INTEGER,i);
    }

    /*public static void updatePlayerQuestsMappings(Player player){
        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        PersistentDataContainer[] quests = persistentDataContainer.get(playerQuests, PersistentDataType.TAG_CONTAINER_ARRAY);
        if(quests == null){

        }
        int[] questReferences = new int[quests.length];
    }*/
}
