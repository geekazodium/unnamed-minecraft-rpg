package com.geekazodium.cavernsofamethyst.entities.treasure;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Random;

public abstract class LootChestHandler implements TreasureEntity{
    protected static int baseRespawnDuration = 20*60*2;
    protected static int respawnVariation = 1200;
    protected int timeSinceLastOpened = -100;
    protected int timeUntilRespawn = 100;
    protected final Random random = new Random();
    protected boolean isPlaced = false;

    @Override
    public void tick() {
        timeSinceLastOpened +=1;
        if(!isPlaced){
            timeUntilRespawn -= 1;
            if(timeUntilRespawn <= 0){
                isPlaced = true;
                location().getBlock().setType(Material.CHEST);
            }
        }else{
            location().getWorld().getNearbyPlayers(location(),10)
                    .forEach(p -> p.spawnParticle(Particle.FIREWORKS_SPARK,location().toCenterLocation(),1,0,0,0,2));
        }
    }

    @Override
    public void interact(Player player) {
        if(!isPlaced){
            return;
        }
        isPlaced = false;
        location().getWorld().playEffect(location(), Effect.WITHER_BREAK_BLOCK,0,10);
        BlockData blockData = location().getBlock().getBlockData();
        location().getWorld().getNearbyPlayers(location(),10)
                .forEach(p -> p.spawnParticle(Particle.BLOCK_CRACK,location(),20, blockData));
        location().getBlock().setType(Material.AIR);
        timeUntilRespawn = random.nextInt(baseRespawnDuration,baseRespawnDuration+respawnVariation);
        timeSinceLastOpened = -timeUntilRespawn;
    }
}
