package com.geekazodium.unnamedminecraftrpg.elementalreactions;

import com.geekazodium.unnamedminecraftrpg.util.ElementalReactionUtil;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.entity.Entity;

public class Extinguish implements Reaction{

    @Override
    public ReactionInstance createInstance(Entity damager) {
        return new ReactionInstance(damager) {

        };
    }

    @Override
    public Pair<Integer, Integer> getElements() {
        return Pair.of(ElementalReactionUtil.WATER,ElementalReactionUtil.FIRE);
    }
}
