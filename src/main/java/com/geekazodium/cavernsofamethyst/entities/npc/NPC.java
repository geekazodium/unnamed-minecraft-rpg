package com.geekazodium.cavernsofamethyst.entities.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface NPC {
    void tick();
    void spawn();
    void despawn();
    Location location();
    Entity getEntity();
}