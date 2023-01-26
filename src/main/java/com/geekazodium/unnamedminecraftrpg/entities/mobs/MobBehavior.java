package com.geekazodium.unnamedminecraftrpg.entities.mobs;

import com.destroystokyo.paper.ParticleBuilder;
import com.geekazodium.unnamedminecraftrpg.Main;
import com.geekazodium.unnamedminecraftrpg.entities.holograms.Hologram;
import com.geekazodium.unnamedminecraftrpg.util.ElementalReactionUtil;
import com.geekazodium.unnamedminecraftrpg.util.EntityDisplayUtil;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class MobBehavior {
    public static final NamespacedKey MOB_DISPLAY_NAME_KEY = new NamespacedKey(Main.getInstance(),"mobdisplayname");
    int entityCap;
    protected List<LivingEntity> entities;
    protected int spawnDelay = 40;
    protected int spawnTick = 0;
    protected HashMap<LivingEntity, Hologram> labels;
    protected final Location center;
    protected final double spawnRadius;
    protected final Random random = new Random();
    private boolean loaded = false;

    public MobBehavior(Location center,int entityCap,double spawnRadius){
        this.center = center;
        this.spawnRadius = spawnRadius;
        this.entityCap = entityCap;
    }

    public Location getCenter() {
        return center;
    }

    public void tick(World world) { //TODO check if entity can't pathfinder back
        if(entities.size()<entityCap){
            spawnTick+=1;
            if(spawnTick > spawnDelay) {
                spawnTick = 0;
                Location spawnLocation = getSpawnLocation();
                if (!spawnLocation.getBlock().isCollidable()) {
                    spawnLocation.add(0.5, 0, 0.5);
                    LivingEntity entity = spawnEntity(world, spawnLocation);
                    Hologram hologram = Hologram.spawnForEntity(entity);
                    playSpawnEffect(entity);
                    entities.add(entity);
                    labels.put(entity, hologram);
                }
            }
        }
        for (LivingEntity entity:entities) {
            entity.customName(EntityDisplayUtil.formatHp(entity.getHealth(), Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(),10));
        }
        entities.removeIf(Entity::isDead);
        List<LivingEntity> toRemove = new ArrayList<>();
        for(Map.Entry<LivingEntity, Hologram> entry:labels.entrySet()){
            LivingEntity entity = entry.getKey();
            Hologram hologram = entry.getValue();
            if(entity.isDead()){
                hologram.remove();
                toRemove.add(entity);
            }
            hologram.teleport(getLabelLocation(entity));
        }
        for (LivingEntity e:toRemove) {
            labels.remove(e);
        }
        ElementalReactionUtil.tickLivingEntities(entities);
    }

    private void playSpawnEffect(LivingEntity entity)  {
        Collection<Player> nearbyPlayers = entity.getLocation().getNearbyPlayers(50);
        @NotNull ParticleBuilder particle = Particle.REDSTONE.builder();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GRAY,3);
        for (Player player:nearbyPlayers) {
            BoundingBox boundingBox = entity.getBoundingBox();
            player.spawnParticle(
                    particle.particle(),
                    boundingBox.getCenterX(),
                    boundingBox.getCenterY(),
                    boundingBox.getCenterZ(),
                    10,
                    boundingBox.getWidthX()/2,
                    boundingBox.getHeight()/2,
                    boundingBox.getWidthZ()/2,
                    0,dustOptions
            );
        }
    }

    @NotNull
    private Location getSpawnLocation() {
        Location spawnLocation = center.clone();
        spawnLocation.setX(center.getX()+ random.nextDouble(-spawnRadius, spawnRadius));
        spawnLocation.setZ(center.getZ()+ random.nextDouble(-spawnRadius, spawnRadius));
        spawnLocation.setY(Math.floor(spawnLocation.getY()));
        spawnLocation.setX(Math.round(spawnLocation.getX()));
        spawnLocation.setZ(Math.round(spawnLocation.getZ()));
        int c=0;
        while (c<spawnRadius){
            if(spawnLocation.getBlock().isCollidable()){
                spawnLocation.add(0,1,0);
            }
            c+=1;
        }
        return spawnLocation;
    }

    @NotNull
    protected abstract LivingEntity spawnEntity(World world,Location location);

    protected void setMobDisplayNameKey(Entity entity,String name){
        entity.getPersistentDataContainer().set(MOB_DISPLAY_NAME_KEY,PersistentDataType.STRING,name);
    }

    private Location getLabelLocation(LivingEntity entity){
        BoundingBox boundingBox = entity.getBoundingBox();
        return new Location(entity.getWorld(), boundingBox.getCenterX(), boundingBox.getMaxY()-1.65, boundingBox.getCenterZ());
    }

    public void onUnload(){
        loaded = false;
        for (LivingEntity e:entities) {
            e.remove();
            labels.get(e).remove();
        }
        entities = null;
        labels = null;
    }

    public void onLoad() {
        loaded = true;
        labels = new HashMap<>();
        entities = new ArrayList<>();
    }

    public boolean isLoaded() {
        return loaded;
    }
}
