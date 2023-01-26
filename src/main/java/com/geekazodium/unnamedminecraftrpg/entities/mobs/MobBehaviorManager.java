package com.geekazodium.unnamedminecraftrpg.entities.mobs;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public abstract class MobBehaviorManager {
    protected World world;
    public final List<MobBehavior> spawningBehaviors = new ArrayList<>();
    public final List<MobBehavior> active = new ArrayList<>();
    public void tick(){
        for (MobBehavior spawner: spawningBehaviors) {
            if(active.contains(spawner)){
                if(!spawner.getCenter().isChunkLoaded()){
                    active.remove(spawner);
                    spawner.onUnload();
                }
            }else {
                if (spawner.getCenter().isChunkLoaded()) {
                    active.add(spawner);
                    spawner.onLoad();
                }
            }
        }
        for (MobBehavior spawner: active) {
            spawner.tick(world);
        }
    }

    public void clear() {
        for (MobBehavior spawner: spawningBehaviors) {
            if(spawner.isLoaded()){
                spawner.onUnload();
            }
        }
    }
}
