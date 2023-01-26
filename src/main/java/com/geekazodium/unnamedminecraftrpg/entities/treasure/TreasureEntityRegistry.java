package com.geekazodium.unnamedminecraftrpg.entities.treasure;

import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.quests.tutorial.TutorialWeaponChest;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TreasureEntityRegistry {
    private static final List<World> loadedWorlds = new ArrayList<>();
    public static final Map<World,WorldTreasureEntityHandler> handlers = new HashMap<>();
    static{
        WorldTreasureEntityHandler overworldHandler = new WorldTreasureEntityHandler(Main.overworld);
        handlers.put(Main.overworld, overworldHandler);
        overworldHandler.put(new TutorialWeaponChest());
        overworldHandler.put(new RandomLootChest(new Location(Main.overworld,-125,72,40)));
        overworldHandler.put(new RandomLootChest(new Location(Main.overworld,-87,72,16)));
        overworldHandler.put(new RandomLootChest(new Location(Main.overworld,-107,63,43)));
        overworldHandler.put(new RandomLootChest(new Location(Main.overworld,-73,62,52)));
    }
    public static void tickHandlers(){
        handlers.forEach((world, worldTreasureEntityHandler) -> {
            if(world.getPlayerCount()>0){
                if (!loadedWorlds.contains(world)){
                    loadedWorlds.add(world);
                    worldTreasureEntityHandler.load();
                }
                worldTreasureEntityHandler.tick();
            }else{
                if(loadedWorlds.contains(world)){
                    loadedWorlds.remove(world);
                    worldTreasureEntityHandler.unload();
                }
            }
        });
    }
}
