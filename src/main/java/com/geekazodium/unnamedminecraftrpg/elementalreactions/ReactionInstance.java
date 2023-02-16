package com.geekazodium.unnamedminecraftrpg.elementalreactions;

import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.entity.Entity;

public abstract class ReactionInstance{
    protected final Entity damager;
    public ReactionInstance(Entity damager){
        this.damager = damager;
    }
    public final void end(){

    }
}
