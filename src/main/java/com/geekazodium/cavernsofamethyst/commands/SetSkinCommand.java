package com.geekazodium.cavernsofamethyst.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.geekazodium.cavernsofamethyst.util.PlayerProfileHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

import static com.geekazodium.cavernsofamethyst.Main.LOGGER;

public class SetSkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0] == null){
            return false;
        }
        try {
            if(sender instanceof Player player) {
                PlayerProfileHelper.setPlayerSkin(player,PlayerProfileHelper.getPlayerProfile(args[0],false));
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
