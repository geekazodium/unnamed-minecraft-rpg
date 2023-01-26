package com.geekazodium.unnamedminecraftrpg.entities.treasure;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface TreasureEntity {
    Location location();
    void tick();
    void interact(Player player);
    boolean canInteract(Player player);
}
