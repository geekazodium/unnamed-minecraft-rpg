package com.geekazodium.unnamedminecraftrpg.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Collection;


public class ParticleUtil {
    public static void line(Player player, Particle particle, int count, Object data, Location p1, Location p2, int points, double offsetX, double offsetY, double offsetZ, double extra){
        for(int i = 0;i<=points;i++){
            player.spawnParticle(particle,lerpPoints(p1,p2,(double) i/(double) points),count,offsetX,offsetY,offsetZ,extra,data);
        }
    }
    public static void line(Collection<Player> players, Particle particle, int count, Object data, Location p1, Location p2, int points, double offsetX, double offsetY, double offsetZ, double extra){
        for(int i = 0;i<=points;i++){
            for (Player player:players){
                player.spawnParticle(particle,lerpPoints(p1,p2,(double) i/(double) points),count,offsetX,offsetY,offsetZ,extra,data);
            }
        }
    }
    private static Location lerpPoints(Location location1, Location location2, double p){
        Location l1 = location1.clone().multiply(p);
        Location l2 = location2.clone().multiply(1-p);
        return l1.add(l2);
    }

    private static double lerp(double a,double b,double p){
        return a*p+b+(1d-p);
    }
}
