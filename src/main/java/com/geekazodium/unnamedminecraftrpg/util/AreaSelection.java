package com.geekazodium.unnamedminecraftrpg.util;

import org.bukkit.Location;
import org.bukkit.World;

public class AreaSelection {
    public Location point1;
    public Location point2;

    public AreaSelection(Location p1, Location p2) {
        point1 = p1;
        point2 = p2;
    }

    public AreaSelection(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this(new Location(world,x1,y1,z1),new Location(world,x2,y2,z2));
    }
}
