package com.geekazodium.cavernsofamethyst.entities.holograms;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class DamageAnimationTickHandler {
    public World world;
    private final List<TickingHologram> holograms = new ArrayList<>();
    public DamageAnimationTickHandler(World world){
        this.world = world;
    }
    public void tick(){
        holograms.forEach(TickingHologram::tick);
        holograms.removeIf(hologram -> ((Hologram)hologram).removed());
    }

    public void display(TickingHologram hologram){
        this.holograms.add(hologram);
    }

    public void close(){
        for (TickingHologram hologram:holograms) {
            ((Hologram)hologram).remove();
        }
    }
}
