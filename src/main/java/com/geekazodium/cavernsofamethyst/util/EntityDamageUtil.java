package com.geekazodium.cavernsofamethyst.util;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.entities.holograms.Hologram;
import com.geekazodium.cavernsofamethyst.entities.holograms.TickingHologram;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.items.weapons.WeaponItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.*;

public class EntityDamageUtil {
    public static final NamespacedKey ENTITY_DAMAGERS = new NamespacedKey(Main.getInstance(),"entity_damagers");
    public static final NamespacedKey DAMAGER_ID = new NamespacedKey(Main.getInstance(),"damager_id");
    public static final NamespacedKey LAST_DAMAGE_TIME = new NamespacedKey(Main.getInstance(),"last_damage_time");
    public static final NamespacedKey CRITICAL_RATE = new NamespacedKey(Main.getInstance(),"critical_damage_rate");
    public static final NamespacedKey CRITICAL_DAMAGE = new NamespacedKey(Main.getInstance(),"critical_damage_bonus");
    public static final NamespacedKey MAX_MANA_KEY = new NamespacedKey(Main.getInstance(),"maximum_mana");
    public static final NamespacedKey BASE_ATTACK_KEY = new NamespacedKey(Main.getInstance(),"attack_base");
    public static final NamespacedKey EFFECTIVE_ATTACK_KEY = new NamespacedKey(Main.getInstance(),"attack_effective");
    public static final NamespacedKey MAX_HEALTH_KEY = new NamespacedKey(Main.getInstance(),"maximum_health");
    public static final NamespacedKey PROJECTILE_WEAPON_HANDLER =new NamespacedKey(Main.getInstance(),"projectile_weapon_handler");

    public static void copyPlayerWeaponDataToProjectile(PersistentDataContainer container, Entity projectile,WeaponItemHandler itemHandler){
        PersistentDataContainer projectileContainer = projectile.getPersistentDataContainer();
        projectileContainer.set(FIRE_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.fireBaseDamage());
        projectileContainer.set(EARTH_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.earthBaseDamage());
        projectileContainer.set(WATER_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.waterBaseDamage());
        projectileContainer.set(NEUTRAL_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.neutralBaseDamage());
        copyIntStatToContainer(BASE_ATTACK_KEY,projectileContainer,container);
        copyIntStatToContainer(EFFECTIVE_ATTACK_KEY,projectileContainer,container);
        copyIntStatToContainer(MAX_MANA_KEY,projectileContainer,container);
        copyIntStatToContainer(MAX_HEALTH_KEY,projectileContainer,container);
        projectileContainer.set(CRITICAL_RATE,PersistentDataType.FLOAT,container.getOrDefault(CRITICAL_RATE,PersistentDataType.FLOAT,0f));
        projectileContainer.set(CRITICAL_DAMAGE,PersistentDataType.FLOAT,container.getOrDefault(CRITICAL_DAMAGE,PersistentDataType.FLOAT,0f));
        projectileContainer.set(PROJECTILE_WEAPON_HANDLER,PersistentDataType.STRING,CustomItemHandlerRegistry.getIdForHandler(itemHandler));

    }
    private static void copyIntStatToContainer(NamespacedKey key,PersistentDataContainer container,PersistentDataContainer playerContainer){
        container.set(key,PersistentDataType.INTEGER,playerContainer.getOrDefault(key,PersistentDataType.INTEGER,0));
    }
    public static void onPlayerDamageEntity(Player damager, LivingEntity entity, WeaponItemHandler itemHandler){
        if(entity.isInvulnerable() || entity instanceof Player || entity.isDead()){return;}
        Random random = new Random();
        //ItemStack item = offhand?damager.getInventory().getItemInOffHand():damager.getInventory().getItemInMainHand();
        PersistentDataContainer container = damager.getPersistentDataContainer();
        playerDamageEntity(damager, entity, random, container,itemHandler);
    }

    private static void playerDamageEntity(Player damager, LivingEntity entity, Random random, PersistentDataContainer container, WeaponItemHandler itemHandler) {
        addPlayerToEntityDamagers(damager,entity.getPersistentDataContainer());
        damageEntity(damager,entity,random,container,itemHandler.fireBaseDamage(),itemHandler.earthBaseDamage(),itemHandler.waterBaseDamage(),itemHandler.neutralBaseDamage());
    }

    private static void addPlayerToEntityDamagers(Player damager,PersistentDataContainer entityContainer){
        String id = damager.getUniqueId().toString();
        PersistentDataContainer[] damagers = entityContainer.get(ENTITY_DAMAGERS, PersistentDataType.TAG_CONTAINER_ARRAY);
        if(damagers == null){
            damagers = new PersistentDataContainer[8];
            for (int i = 0;i<8;i++){
                damagers[i] = new ItemStack(Material.ARROW).getItemMeta().getPersistentDataContainer();
            }
        }
        for (int i = 0;i<8;i++) {
            PersistentDataContainer player = damagers[i];
            if(!isPlayerDamageTimeValid(player,Bukkit.getCurrentTick())){
                player = new ItemStack(Material.ARROW).getItemMeta().getPersistentDataContainer();
                player.set(DAMAGER_ID,PersistentDataType.STRING,id);
                updateLastDamageTime(player);
                damagers[i] = player;
                entityContainer.set(ENTITY_DAMAGERS,PersistentDataType.TAG_CONTAINER_ARRAY,damagers);
                return;
            } else {
                String string = player.get(DAMAGER_ID, PersistentDataType.STRING);
                if (string != null && string.equals(id)) {
                    updateLastDamageTime(player);
                    entityContainer.set(ENTITY_DAMAGERS,PersistentDataType.TAG_CONTAINER_ARRAY,damagers);
                    return;
                }
            }
        }
    }

    public static List<Player> getEntityDamagedBy(PersistentDataContainer entityContainer){
        //PersistentDataContainer entityContainer = entity.getPersistentDataContainer();
        List<Player> damagedBy = new ArrayList<>();
        PersistentDataContainer[] damagers = entityContainer.get(ENTITY_DAMAGERS, PersistentDataType.TAG_CONTAINER_ARRAY);
        if(damagers == null){
            return damagedBy;
        }
        int currentTick = Bukkit.getServer().getCurrentTick();
        for (PersistentDataContainer player : damagers) {
            if(isPlayerDamageTimeValid(player,currentTick)){
                String string = player.get(DAMAGER_ID, PersistentDataType.STRING);
                if(string == null){
                    continue;
                }
                damagedBy.add(Bukkit.getServer().getPlayer(UUID.fromString(string)));
            }
        }
        return damagedBy;
    }

    public static boolean isPlayerDamageTimeValid(PersistentDataContainer container,int currentTick){
        return currentTick - container.getOrDefault(LAST_DAMAGE_TIME,PersistentDataType.INTEGER,0)<=400;
    }

    private static void updateLastDamageTime(PersistentDataContainer container){
        container.set(LAST_DAMAGE_TIME,PersistentDataType.INTEGER,Bukkit.getCurrentTick());
    }

    public static void onPlayerDamageEntity(Player damager, PersistentDataContainer container, LivingEntity entity, WeaponItemHandler itemHandler){
        if(entity.isInvulnerable() || entity instanceof Player || entity.isDead()){return;}
        Random random = new Random();
        playerDamageEntity(damager,entity,random,container,itemHandler);
    }

    public static void onPlayerProjectileDamageEntity(Player damager,PersistentDataContainer container, LivingEntity entity){
        onPlayerDamageEntity(damager,container,entity,
                (WeaponItemHandler) CustomItemHandlerRegistry.getFromId(container.get(PROJECTILE_WEAPON_HANDLER,PersistentDataType.STRING))
        );
    }

    private static void damageEntity(Entity damager, LivingEntity entity, Random random, PersistentDataContainer container,int baseFire,int baseEarth,int baseWater,int baseNeutral) {
        float criticalPercent = container.getOrDefault(CRITICAL_RATE,PersistentDataType.FLOAT,0f);
        boolean critical = criticalPercent*100f > random.nextFloat(0,100);
        float critDmg = container.getOrDefault(CRITICAL_DAMAGE,PersistentDataType.FLOAT,0f);
        int fire = defaultDamageRoll(baseFire,critical, critDmg,random);
        int earth = defaultDamageRoll(baseEarth,critical,critDmg, random);
        int water = defaultDamageRoll(baseWater,critical,critDmg, random);
        int neutral = neutralDamageRoll(baseNeutral,critical,critDmg,random);
        int element = getElement(random, fire, earth, water);
        int elementCap = getElementalPower((element == FIRE)?fire:((element == EARTH)?earth:water));
        int entityElement = getElementForEntity(entity,random);
        int baseAttack = container.getOrDefault(BASE_ATTACK_KEY, PersistentDataType.INTEGER, 0);
        int effectiveAttack = container.getOrDefault(EFFECTIVE_ATTACK_KEY, PersistentDataType.INTEGER, 0);
        int maxHealth = container.getOrDefault(MAX_HEALTH_KEY, PersistentDataType.INTEGER, 0);
        int maxMana = container.getOrDefault(MAX_MANA_KEY, PersistentDataType.INTEGER, 0);
        if (entityElement != -1) {
            if (element == FIRE) {
                if (entityElement == EARTH){
                    fire += maxMana;
                }
                if (entityElement == WATER){
                    fire += maxHealth;
                }
            }else if(element == EARTH){
                if (entityElement == FIRE){
                    earth += baseAttack;
                }
                if(entityElement == WATER){
                    earth += maxHealth;
                }
            }else if(element == WATER){
                if(entityElement == FIRE){
                    water += baseAttack;
                }
                if (entityElement == EARTH){
                    water += maxMana;
                }
            }
        }
        fire += fire * effectiveAttack/10;
        earth += earth * effectiveAttack/10;
        water += water * effectiveAttack/10;
        neutral += neutral * effectiveAttack/8;
        /*if(damager instanceof Player player) { code to debug player stats
            debugCharacterStats(player, new String[]{
                    String.valueOf(fire),
                    String.valueOf(earth),
                    String.valueOf(water),
                    String.valueOf(baseAttack),
                    String.valueOf(maxMana),
                    String.valueOf(maxHealth)
            });
        }*/
        TickingHologram damageHologram = (TickingHologram) Hologram.spawnForDamageEvent(damager, entity,fire,earth,water,neutral,element);
        GameTickHandler.getInstance().overworldDamageAnimationTickHandler.display(damageHologram);
        applyElement(entity, element, elementCap,50*(critical?2:1));
        entity.setNoDamageTicks(0);
        entity.damage(fire+earth+water+neutral);
    }

    private static int neutralDamageRoll(float damage, boolean critical, float critDmg, Random random) {
        if(!(damage>0)){
            return 0;
        }
        return (int) (
                (
                        damage+
                                random.nextFloat(0.5f)*Math.log(damage)+
                                random.nextFloat(1f)
                )*(critical?critDmg+1:1)
        );
    }

    private static void debugCharacterStats(Player player,String[] data){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : data) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        player.sendMessage(stringBuilder.substring(0,stringBuilder.length()-2));
    }

    private static int getElementalPower(int damage) {
        return (int) (Math.sqrt(damage)*100d);
    }

    private static int defaultDamageRoll(float damage,boolean critical,float criticalBonus,Random random){
        if(!(damage>0)){
            return 0;
        }
        return (int) (
                (
                        damage+
                        random.nextFloat(1f)*Math.log(damage)+
                        random.nextFloat(2f)
                )*(critical?criticalBonus+1:1)
        );
    }

    public static void onEntityDamagePlayer(Entity damager, Player player) {
        Random random = new Random();
        EntityDamagePlayer(damager, player,random,damager.getPersistentDataContainer());
    }

    private static void EntityDamagePlayer(Entity damager, Player player, Random random, PersistentDataContainer persistentDataContainer) {
        PersistentDataContainer entityContainer = damager.getPersistentDataContainer();
        int fire = entityContainer.getOrDefault(FIRE_BASE_DAMAGE,PersistentDataType.INTEGER,0);
        int earth = entityContainer.getOrDefault(EARTH_BASE_DAMAGE,PersistentDataType.INTEGER,0);
        int water = entityContainer.getOrDefault(WATER_BASE_DAMAGE,PersistentDataType.INTEGER,0);
        int neutral = entityContainer.getOrDefault(NEUTRAL_BASE_DAMAGE,PersistentDataType.INTEGER,0);
        damageEntity(damager,player,random,persistentDataContainer,fire,earth,water,neutral);
    }
}
