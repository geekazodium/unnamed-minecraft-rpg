package com.geekazodium.unnamedminecraftrpg.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

public class EntityDisplayUtil {
    public static Component formatHp(double health, double max, int length){
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text("[").style(Style.style(TextColor.color(0x881111))));
        builder.append(Component.text(new String(new char[(int) Math.floor(health/max*length)]).replace("\0", "|")).style(Style.style(TextColor.color(0xaa2222))));
        builder.append(Component.text(new String(new char[(int) Math.ceil((max-health)/max*length)]).replace("\0", "|")).style(Style.style(TextColor.color(0xffffff))));
        builder.append(Component.text("]").style(Style.style(TextColor.color(0x881111))));
        return builder.build();
    }
    public static Component formatName(String name){
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text("[Hostile]").style(Style.style(TextColor.color(0x44ff99))));
        builder.append(Component.text(name).style(Style.style(TextColor.color(0x3aa00a))));
        return builder.build();
    }
}
