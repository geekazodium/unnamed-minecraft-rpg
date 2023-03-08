package com.geekazodium.unnamedminecraftrpg.util;

import com.geekazodium.unnamedminecraftrpg.elementalreactions.Reaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Random;

public class DamageInstance {

    public static final int TOTAL_DAMAGE_TYPES = 4;
    private final int[] damage = new int[TOTAL_DAMAGE_TYPES];
    private final Entity damager;
    public final int[] finalDamage = new int[TOTAL_DAMAGE_TYPES];
    boolean critical;
    float criticalBonus;
    float criticalRate;

    public DamageInstance(int[] baseDamage,Entity damager){
        this.damager = damager;
        System.arraycopy(baseDamage, 0, damage, 0, Math.min(TOTAL_DAMAGE_TYPES, baseDamage.length));
    }
    public void apply(LivingEntity entity,Random random) {
        int damage = 0;
        int element = ElementalReactionUtil.getElementForEntity(entity, random);
        Reaction reaction = ElementalReactionUtil.getReaction(getDamageInstanceElement(random), element);
        if(reaction != null){
            reaction.createInstance(damager);
        }
        for (int e = 0;e<TOTAL_DAMAGE_TYPES;e++) {
            damage+= this.finalDamage[e];
        }
        if(damage<=0)return;
        entity.setNoDamageTicks(0);
        entity.damage(damage);
    }

    public void rollFinalDamage(Random random) {
        rollCritical(random);
        for (int e = 0; e < TOTAL_DAMAGE_TYPES; e++) {
            finalDamage[e]=rollDamage(e, random);
        }
    }

    public void applyAttackBonus(int effectiveAttack){
        for (int e = 0; e < TOTAL_DAMAGE_TYPES; e++) {
            damage[e]+= (double) damage[e] * (double) effectiveAttack /100d;
        }
    }

    public void setCriticalRate(float criticalRate){
        this.criticalRate = criticalRate;
    }
    public void rollCritical(Random random){
        setCritical(criticalRate*100f > random.nextFloat(0,100));
    }
    public void setCritical(boolean isCritical) {
        critical = isCritical;
    }

    public void setCriticalBonus(float criticalBonus){
        this.criticalBonus = criticalBonus;
    }

    public int rollDamage(int element, Random random){
        int damage = this.damage[element];
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

    public int getDamageInstanceElement(Random random){
        int i = -1;
        int highestDamage = 0;
        double duplicates = 1;
        for(int e = 0;e<TOTAL_DAMAGE_TYPES;e++){
            if(damage[e]>highestDamage){
                highestDamage=damage[e];
                i = e;
                duplicates = 1;
            } else if (damage[e] == highestDamage) {
                duplicates++;
                if(random.nextFloat()<1f/(float)duplicates){
                    i = e;
                }
            }else{
                duplicates = 1;
            }
        }
        return i;
    }
}
