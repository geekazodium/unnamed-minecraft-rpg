package com.geekazodium.unnamedminecraftrpg.entities.mobs.pathfinder;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.geekazodium.unnamedminecraftrpg.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Random;

import static com.geekazodium.unnamedminecraftrpg.util.RandomUtil.nextIntRandomSign;

public class IdleLimitedMobGoal implements Goal<Mob>, Listener{
    private final GoalKey<Mob> key = GoalKey.of(Mob.class,new NamespacedKey(Main.getInstance(),"idle"));
    public Location location;
    private final Random random = new Random();
    private Location targetLocation;
    private int focusTimer = 0;
    private boolean idle = false;
    private final int distance;
    private final Mob mob;
    public IdleLimitedMobGoal(Mob mob, Location location, double distance){
        this.location = location;
        this.distance = (int) distance;
        this.mob = mob;
        targetLocation = this.location.toCenterLocation();
    }
    @Override
    public boolean shouldActivate() {
        return location.getNearbyPlayers(64).size()>0;//mob.getLocation().distanceSquared(location)>distanceSquared;
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
        if(!idle) {
            mob.getPathfinder().moveTo(targetLocation);
            if (focusTimer<=0||isClose(mob.getLocation(), targetLocation)) {
                targetLocation = getTargetLocation();
                focusTimer = random.nextInt(10,60);
                idle=!idle;
            }else{
                focusTimer--;
            }
        }else{
            if(focusTimer<=0){
                focusTimer = random.nextInt(20,60);
                idle=!idle;
            }else{
                focusTimer--;
            }
        }
    }

    private Location getTargetLocation(){
        return location.clone().toCenterLocation().add(
                nextIntRandomSign(distance,random),0,nextIntRandomSign(distance,random)
        );
    }

    private boolean isClose(Location a,Location b){
        double x=a.getX()-b.getX();
        double z=a.getZ()-b.getZ();
        return (x*x+z*z)< 1;
    }
}
