package com.geekazodium.unnamedminecraftrpg.util;

import org.bukkit.util.Vector;

public class VecUtil {
    public static Vector getVectorFor(double pitch, double yaw, double length){
        double y = Math.sin(pitch);
        double _y = Math.cos(pitch);
        double x = Math.sin(yaw)*_y;
        double z = Math.cos(yaw)*_y;
        return new Vector(x*length,y*length,z*length);
    }

    public static Vector getVectorFor(double pitch, double yaw, double length,boolean degrees){
        if(degrees){
            return getVectorFor(Math.toRadians(pitch),Math.toRadians(yaw),length);
        }else{
            return getVectorFor(pitch,yaw,length);
        }
    }
}