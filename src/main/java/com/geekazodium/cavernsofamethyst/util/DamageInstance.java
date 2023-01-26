package com.geekazodium.cavernsofamethyst.util;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.entities.holograms.Hologram;
import com.geekazodium.cavernsofamethyst.entities.holograms.TickingHologram;
import org.bukkit.entity.LivingEntity;

import java.util.Random;

public class DamageInstance {

    public static final int TOTAL_DAMAGE_TYPES = 4;
    private final int[] damage = new int[TOTAL_DAMAGE_TYPES];
    public final int[] finalDamage = new int[TOTAL_DAMAGE_TYPES];
    boolean critical;
    float criticalBonus;
    float criticalRate;

    public DamageInstance(int[] baseDamage){
        System.arraycopy(baseDamage, 0, damage, 0, Math.min(TOTAL_DAMAGE_TYPES, baseDamage.length));
    }
    public void apply(LivingEntity entity) {
        int damage = 0;
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
}
