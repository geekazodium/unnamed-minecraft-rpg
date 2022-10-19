package com.geekazodium.cavernsofamethyst.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.geekazodium.cavernsofamethyst.Main.LOGGER;

public class SetSkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if(sender instanceof Player player) {
                PlayerProfile playerProfile = player.getPlayerProfile();
                /*playerProfile.setProperty(new ProfileProperty(
                        "textures",
                        "ewogICJ0aW1lc3RhbXAiIDogMTY2NDcwMDc1MzMwOCwKICAicHJvZmlsZUlkIiA6ICI1MDhhZjZmY2E0YmI0MWRlOWFlOWZkMWZmMmE0YjYwMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZWVrYXpvZGl1bSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83MWUyMDM0NDA0ZDExNzQ3YjYyMWQ2YTljMDllOWRlZTg0Njc3YzY4N2E5YmIyZGI2MzJhMGI3NmFlMDY5NWIwIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                        "tgEIUzQByrV43Ne2laBpayrosoPOuW5+y53CWl8gNwZ23gTWvbsTQeAMLVt0Hnd/5GeGCfhoGfqrh+PPDgjNE7YuFiKdYvgWyNMCAOsfu062z8AwjN6EfelYgdCZOkkPKhyNxke5V3+vlTVC9ZuIgSYamgU7pzI9UXO7qk3u9U4fOAP1uVU6OGrNlxSLCz3ViVhPmNldYmdcjnKmPdfVNkzr/KxgNWcM75VI7AESz9yBQqOY1MqMmPusiPwrFm4qL7hX50gVdn0rnljdKlIwrg6uYQZiNKVNgqpU43AeHrRmOw5anvFZpAaqw/hdJbwunfaG4sUQrQoDIpsPw3LTbFId/WLbJ0NKx7ZM4IXUHAmRrbW3c6ZyzzNwFjwinnb2XlVF1n2DD1PedAEmPj33JD8OOX9r4bTi9PiFMrl/U9NtBE5n921mzAB0okg0n7v6OuQafwD4dDiXzvbPlQ65qEbsmtOj7fCH3Saj4qkwClVVJ/5uWiEM9IsqG4XL1iNsyYiF7cxPbdcncXo+hovJFcKZvWwRZarDWODFt1jGmy9F2EYtQybSBo1aTIQ8OfODjs0XeyVSojJLP5UQdmkmbY5QLkRtN3OnbnugR0E46hJFyWcD95+/7iD0oiavoOdk08tfiNnyYBev2qSW1O2IbqlR0CzHkVwjWCxlxp1j0ak="
                ));*/
                playerProfile.setProperty(new ProfileProperty(
                        "textures",
                        "ewogICJ0aW1lc3RhbXAiIDogMTY2NDcwMjkzODQzMywKICAicHJvZmlsZUlkIiA6ICJiYzU5Y2M1NTFlYTE0Njk4YmMyZDBiNTExOGQ2ZWZlMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWeGlkU3RhdGljIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc2YWUwZWIwZTUwOGVkZDI5OTllZGVkNGM1NjhmZTQzY2IzYWFkMjFjMDVkODg5MjliOGY0MDYxYWQxNmM3ZjkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                        "RsuSg/dLfGAzDpbuxtTJ0H/XfD+Y0dMsIaJG4Ywb3D4LPdPPro1BpCRrAfTH2xIQWA/lPdSeUv4GKHKPsB3k6p8uumzX2jngxHFTuV9im6Skq6aI/9sPSn0RYebTKzsPc2JRoytKlaSkkBEZX98sXdgGGlR8G0SpLTAwgM9ws2eTZUFIpRxYKlafhPB1L6pmj7ypXXH/I+dCwwV7DBWBoSId/dz1bbarcpyGCiv51TsAmQ7gevpcJNsPpnlIFSII3kZxBt6+dwhEngOoa6UikZeO27Fr0ekEYE4vs6FIYDdVkQVeoQPhs0ZJ8jwkuhjFQ7Q5CB+1Q4z5kz52cxzdH6AQy0kyjwD8lZFy1uZ4wrJvj9Z7ssnLL+5d8oRNF3ZYIwZgzFbpGVb24he+mqRbuRofwtCcav6fyIDSDUElLJMP0aOHovJ4uiTRCwJEufE00EAmheTmfJMMVUNstuZcZKTuV/mZbQO4IN7+IgK7DoBdwb+Mpo5OoPhIDVuLHH+Z4vv3sKMZ+8bdyGaOHNGT7kZdrJZUG2an6KnjYboWXD0cl1DzWSb7SPcttEoHlSuo+z1ePTocFDT19/7XVvSVJQgzQV/AqNS6gLjqEzGVfUL2d1PTpIfC1dovhAmwyklsV8UR260CTM5lDq+nyIhnEhJn4DPT2ZhqZOsgVCw2FBA="
                ));
                player.setPlayerProfile(playerProfile);
                return true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    /*public static void setPlayerSkin(Player player, @Nullable String uuid){
        if(uuid == null){
            PlayerProfile playerProfile = player.getPlayerProfile();
            playerProfile.clearProperties();
            player.setPlayerProfile(playerProfile);
        }
        else(){
        }
    }*/
}
