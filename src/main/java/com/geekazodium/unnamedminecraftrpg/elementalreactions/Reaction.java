package com.geekazodium.unnamedminecraftrpg.elementalreactions;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.entity.Entity;

public interface Reaction {
    ReactionInstance createInstance(Entity damager);

    Pair<Integer, Integer> getElements();
}
