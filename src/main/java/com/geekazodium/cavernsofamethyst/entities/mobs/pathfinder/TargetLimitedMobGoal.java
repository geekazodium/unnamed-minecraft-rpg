package com.geekazodium.cavernsofamethyst.entities.mobs.pathfinder;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.util.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.EnumSet;
import java.util.Random;

public class TargetLimitedMobGoal implements Goal<Mob>, Listener{
    private final GoalKey<Mob> key = GoalKey.of(Mob.class,new NamespacedKey(Main.getInstance(),"area_limit_goal"));
    public Location location;
    public Player target;
    public int atkCooldown = 0;
    private final Random random = new Random();
    private final int distance;
    private final Mob mob;
    public TargetLimitedMobGoal(Mob mob, Location location, double distance){
        this.location = location;
        this.distance = (int) distance;
        this.mob = mob;
    }
    @Override
    public boolean shouldActivate() {
        return !location.getNearbyPlayers(distance).isEmpty();
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this,Main.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
        Goal.super.stop();
    }

    @Override
    public void tick() {
        if (atkCooldown > 0) {
            atkCooldown -=1;
        }
        if (target == null) {
            target = RandomUtil.randomPickFromList(location.getNearbyPlayers(distance), random);
        }else {
            if(location.distance(target.getLocation())>distance) {
                target = null;
            }else{
                mob.getPathfinder().moveTo(target);
                if(atkCooldown<=0) {
                    if (mob.getBoundingBox().clone().expand(0.5).overlaps(target.getBoundingBox())) {
                        mob.attack(target);
                        mob.swingMainHand();
                        atkCooldown = 10;
                    }
                }
            }
        }
    }
}
