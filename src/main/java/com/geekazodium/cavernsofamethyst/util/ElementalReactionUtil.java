package com.geekazodium.cavernsofamethyst.util;

import com.geekazodium.cavernsofamethyst.Main;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ElementalReactionUtil {

    public static final int FIRE = 0;

    public static final int EARTH = 1;

    public static final int WATER = 2;
    private static final NamespacedKey entityElementsContainer =
            new NamespacedKey(Main.getInstance(),"entity_element_container");
    public static final NamespacedKey FIRE_BASE_DAMAGE =
            new NamespacedKey(Main.getInstance(),"fire_base_damage");
    public static final NamespacedKey EARTH_BASE_DAMAGE =
            new NamespacedKey(Main.getInstance(),"earth_base_damage");
    public static final NamespacedKey WATER_BASE_DAMAGE =
            new NamespacedKey(Main.getInstance(),"water_base_damage");
    public static final NamespacedKey NEUTRAL_BASE_DAMAGE =
            new NamespacedKey(Main.getInstance(),"neutral_base_damage");
    public static final int totalElements=3;

    public static void tickEntities(List<Entity> entities){
        for (Entity entity:entities) {
            tickEntity(entity);
        }
    }

    public static void tickLivingEntities(List<LivingEntity> entities) {
        for (Entity entity:entities) {
            tickEntity(entity);
        }
    }

    public static boolean applyElement(Entity entity,int element,int dur){
        int[] elementsArray = getElementsArray(entity);
        if(elementsArray == null){
            return false;
        }
        if(elementsArray[element]<= dur) {
            elementsArray[element] = dur;
            entity.getPersistentDataContainer().set(entityElementsContainer, PersistentDataType.INTEGER_ARRAY,elementsArray);
            return true;
        }
        return false;
    }
    public static int[] getElementsArray(Entity entity){
        PersistentDataContainer entityDataContainer = entity.getPersistentDataContainer();
        int @Nullable [] array = entityDataContainer.get(entityElementsContainer, PersistentDataType.INTEGER_ARRAY);
        if(array == null ||array.length<totalElements){
            return null;
        }
        return array;
    }

    public static boolean applyElement(Entity entity,int element,int cap,int power){
        int[] elementsArray = getElementsArray(entity);
        if(elementsArray == null){
            return false;
        }
        if(elementsArray[element]<cap) {
            elementsArray[element]+=Math.min(power,cap-elementsArray[element]);
            entity.getPersistentDataContainer().set(entityElementsContainer, PersistentDataType.INTEGER_ARRAY,elementsArray);
            return true;
        }
        return false;
    }

    public static void tickEntity(Entity entity){
        PersistentDataContainer entityDataContainer = entity.getPersistentDataContainer();
        int[] array = getEntityElementsArray(entityDataContainer);
        for (int e=0;e<array.length;e++) {
            if(array[e]<=0){
                continue;
            }
            array[e]-=1;
        }
        displayElementalEffects(entity,array);
        entityDataContainer.set(entityElementsContainer, PersistentDataType.INTEGER_ARRAY,array);
    }

    private static int[] getEntityElementsArray(PersistentDataContainer entityDataContainer){
        int @Nullable [] array = entityDataContainer.get(entityElementsContainer, PersistentDataType.INTEGER_ARRAY);
        if (array == null || array.length != totalElements){
            array=new int[totalElements];
        }
        return array;
    }

    public static int getElement(Random random, int fire, int earth, int water) {
        int element;
        if(fire > earth){
            element = ifLargerEqualOrSmaller(water, fire,WATER, random.nextBoolean()?WATER:FIRE,FIRE);
        }else if(fire == earth){
            element = ifLargerEqualOrSmaller(water, fire,
                    WATER,ifLargerEqualOrSmaller(random.nextInt(3),1,FIRE,WATER,EARTH),
                    random.nextBoolean()?EARTH:FIRE
            );
        }else{
            element = ifLargerEqualOrSmaller(water, earth,WATER, random.nextBoolean()?WATER:EARTH,EARTH);
        }
        return element;
    }

    private static int ifLargerEqualOrSmaller(int v1,int v2, int l, int e, int s){
        return (v1>v2)?l:(v1 == v2)?e:s;
    }

    public static int getElementForEntity(LivingEntity entity, Random random) {
        PersistentDataContainer entityDataContainer = entity.getPersistentDataContainer();
        int[] array = getEntityElementsArray(entityDataContainer);
        if(array[0]==0&&array[1]==0&&array[2]==0){
            return -1;
        }
        return getElement(random,array[0],array[1],array[2]);
    }

    public static void displayElementalEffects(Entity entity,int[] array){
        BoundingBox boundingBox = entity.getBoundingBox();
        double centerX = boundingBox.getCenterX();
        double centerY = boundingBox.getCenterY();
        double centerZ = boundingBox.getCenterZ();
        double offsetX = boundingBox.getWidthX() / 2;
        double offsetY = boundingBox.getHeight() / 4;
        double offsetZ = boundingBox.getWidthZ() / 2;
        if(array[0]>0){
            for(Player player:entity.getLocation().getNearbyPlayers(100)){
                player.spawnParticle(Particle.FLAME,
                        centerX, centerY, centerZ, 10,
                        offsetX, offsetY, offsetZ, 0
                );
            }
        }
        if(array[1]>0){
            for(Player player:entity.getLocation().getNearbyPlayers(100)){
                player.spawnParticle(Particle.REDSTONE,
                        centerX, centerY, centerZ, 10,
                        offsetX, offsetY, offsetZ,
                        new Particle.DustOptions(Color.fromRGB(0x00ff22),1)
                );
            }
        }
        if(array[2]>0){
            for(Player player:entity.getLocation().getNearbyPlayers(100)){
                player.spawnParticle(Particle.DRIP_WATER,
                        centerX, centerY, centerZ, 10,
                        offsetX, offsetY, offsetZ
                );
            }
        }
    }
}
