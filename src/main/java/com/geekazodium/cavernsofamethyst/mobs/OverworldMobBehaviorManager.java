package com.geekazodium.cavernsofamethyst.mobs;

import org.bukkit.Location;
import org.bukkit.World;

import static com.geekazodium.cavernsofamethyst.Main.overworld;

public class OverworldMobBehaviorManager extends MobBehaviorManager {
    public OverworldMobBehaviorManager(){
        world = overworld;
        spawningBehaviors.add(new ZombieSpawnMobBehavior(new Location(overworld,-131,73,24),3,5));
        spawningBehaviors.add(new TutorialElementZombieBehavior(new Location(overworld,-106,69,26),5,5));
        spawningBehaviors.add(new ZombieSpawnMobBehavior(new Location(overworld,0,50,0),10,20));
        spawningBehaviors.add(new ZombieSpawnMobBehavior(new Location(overworld,200,56,200),10,20));
    }
}
