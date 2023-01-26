package com.geekazodium.unnamedminecraftrpg.entities.holograms;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static com.geekazodium.unnamedminecraftrpg.util.ElementalReactionUtil.*;

public class DamageHologram extends HologramHandler implements TickingHologram{
    public static final Vector GRAVITY = new Vector(0, 0.1, 0);
    private final @NotNull Location location;
    int timeAlive;
    private final Vector velocity;
    private boolean spawned = false;
    private Component display;

    public DamageHologram(Entity damager, LivingEntity entity,int fire, int earth, int water, int neutral, int damageElement){
        display = Component.text("");
        boolean separator = false;
        if(fire>0) {
            Component fireDamage = Component.text(fire);
            Style style = fireDamage.style();
            style = style.color(TextColor.color(255, 44, 0));
            if(damageElement == FIRE){
                style = style.decorate(TextDecoration.BOLD);
            }
            fireDamage = fireDamage.style(style);
            display = display.append(fireDamage);
            separator = true;
        }
        if(earth>0) {
            if(separator){
                display = display.append(Component.space());
            }
            Component earthDamage = Component.text(earth);
            Style style = earthDamage.style();
            style = style.color(TextColor.color(0, 255, 27));
            if(damageElement == EARTH){
                style = style.decorate(TextDecoration.BOLD);
            }
            earthDamage = earthDamage.style(style);
            display = display.append(earthDamage);
            separator = true;
        }
        if(water>0) {
            if(separator){
                display = display.append(Component.space());
            }
            Component waterDamage = Component.text(water);
            Style style = waterDamage.style();
            style = style.color(TextColor.color(0, 106, 255));
            if(damageElement == WATER){
                style = style.decorate(TextDecoration.BOLD);
            }
            waterDamage = waterDamage.style(style);
            display = display.append(waterDamage);
            separator = true;
        }
        if(neutral>0) {
            if(separator){
                display = display.append(Component.space());
            }
            Component neutralDamage = Component.text(neutral);
            Style style = neutralDamage.style();
            style = style.color(TextColor.color(156, 166, 169));
            if(damageElement == NEUTRAL){
                style = style.decorate(TextDecoration.BOLD);
            }
            neutralDamage = neutralDamage.style(style);
            display = display.append(neutralDamage);
        }
        Random random = new Random();
        location = entity.getBoundingBox().getCenter().add(new Vector(0,-1.5,0).add(new Vector(
                random.nextFloat(0f,0.5f)*(random.nextBoolean()?1:-1),
                random.nextFloat(0f,0.5f)*(random.nextBoolean()?1:-1),
                random.nextFloat(0f,0.5f)*(random.nextBoolean()?1:-1)
        ))).toLocation(entity.getWorld());
        velocity = new Vector(
                random.nextFloat(0.05f,0.1f)*(random.nextBoolean()?1:-1),
                random.nextFloat(0.3f,0.5f),
                random.nextFloat(0.05f,0.1f)*(random.nextBoolean()?1:-1)
        );
    }

    @Override
    public void tick(){
        if(!spawned){
            spawned = true;
            this.spawn(display, location);
            timeAlive = 0;
        }
        this.armorStand.teleport(armorStand.getLocation().add(velocity));
        this.velocity.subtract(GRAVITY);
        timeAlive += 1;
        if(timeAlive>100){
            remove();
        }
    }
}
