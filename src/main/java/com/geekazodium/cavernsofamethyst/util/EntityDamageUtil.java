package com.geekazodium.cavernsofamethyst.util;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.holograms.Hologram;
import com.geekazodium.cavernsofamethyst.holograms.TickingHologram;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.items.WeaponItemHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

import static com.geekazodium.cavernsofamethyst.util.ElementalReactionUtil.*;

public class EntityDamageUtil {
    public static final NamespacedKey CRITICAL_RATE = new NamespacedKey(Main.getInstance(),"critical_damage_rate");
    public static final NamespacedKey CRITICAL_DAMAGE = new NamespacedKey(Main.getInstance(),"critical_damage_bonus");
    public static final NamespacedKey MAX_MANA_KEY = new NamespacedKey(Main.getInstance(),"maximum_mana");
    public static final NamespacedKey BASE_ATTACK_KEY = new NamespacedKey(Main.getInstance(),"attack_base");
    public static final NamespacedKey EFFECTIVE_ATTACK_KEY = new NamespacedKey(Main.getInstance(),"attack_effective");
    public static final NamespacedKey MAX_HEALTH_KEY = new NamespacedKey(Main.getInstance(),"maximum_health");
    public static final NamespacedKey PROJECTILE_WEAPON_HANDLER =new NamespacedKey(Main.getInstance(),"projectile_weapon_handler");

    public static void copyPlayerWeaponDataToProjectile(PersistentDataContainer container, Projectile projectile,WeaponItemHandler itemHandler){
        PersistentDataContainer projectileContainer = projectile.getPersistentDataContainer();
        projectileContainer.set(FIRE_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.fireBaseDamage());
        projectileContainer.set(EARTH_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.earthBaseDamage());
        projectileContainer.set(WATER_BASE_DAMAGE,PersistentDataType.INTEGER,itemHandler.waterBaseDamage());
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
        damageEntity(damager, entity, random, container,itemHandler);
    }

    private static void damageEntity(Player damager, LivingEntity entity, Random random, PersistentDataContainer container, WeaponItemHandler itemHandler) {
        damageEntity(damager,entity,random,container,itemHandler.fireBaseDamage(),itemHandler.earthBaseDamage(),itemHandler.waterBaseDamage());
    }

    public static void onPlayerDamageEntity(Player damager, PersistentDataContainer container, LivingEntity entity, WeaponItemHandler itemHandler){
        if(entity.isInvulnerable() || entity instanceof Player || entity.isDead()){return;}
        Random random = new Random();
        damageEntity(damager,entity,random,container,itemHandler);
    }

    public static void onPlayerProjectileDamageEntity(Player damager,PersistentDataContainer container, LivingEntity entity){
        onPlayerDamageEntity(damager,container,entity,
                (WeaponItemHandler) CustomItemHandlerRegistry.getFromId(container.get(PROJECTILE_WEAPON_HANDLER,PersistentDataType.STRING))
        );
    }

    private static void damageEntity(Entity damager, LivingEntity entity, Random random, PersistentDataContainer container,int baseFire,int baseEarth,int baseWater) {
        float criticalPercent = container.getOrDefault(CRITICAL_RATE,PersistentDataType.FLOAT,0f);
        boolean critical = criticalPercent*100f > random.nextFloat(0,100);
        float critDmg = container.getOrDefault(CRITICAL_DAMAGE,PersistentDataType.FLOAT,0f);
        int fire = defaultDamageRoll(baseFire,critical, critDmg,random);
        int earth = defaultDamageRoll(baseEarth,critical,critDmg, random);
        int water = defaultDamageRoll(baseWater,critical,critDmg, random);
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
        fire += fire * effectiveAttack/100;
        earth += earth * effectiveAttack/100;
        water += water * effectiveAttack/100;
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
        TickingHologram damageHologram = (TickingHologram) Hologram.spawnForDamageEvent(damager, entity,fire,earth,water,element);
        GameTickHandler.getInstance().overworldDamageAnimationTickHandler.display(damageHologram);
        applyElement(entity, element, elementCap,50*(critical?2:1));
        entity.setNoDamageTicks(0);
        entity.damage(fire+earth+water);
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
        damageEntity(damager,player,random,persistentDataContainer,fire,earth,water);
    }
}
