package com.geekazodium.unnamedminecraftrpg.listeners;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkLoadingListener implements Listener {
    @EventHandler
    public void onEvent(ChunkLoadEvent event){
        if(GameTickHandler.getInstance() == null){return;}
        GameTickHandler.getInstance().onLoadChunk(event);
    }
    @EventHandler
    public void onEvent(ChunkUnloadEvent event){
        if(GameTickHandler.getInstance() == null){return;}
        GameTickHandler.getInstance().onUnloadChunk(event);
    }
}
