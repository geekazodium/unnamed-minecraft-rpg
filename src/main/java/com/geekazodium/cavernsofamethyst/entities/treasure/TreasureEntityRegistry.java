package com.geekazodium.cavernsofamethyst.entities.treasure;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.quests.tutorial.TutorialWeaponChest;
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
