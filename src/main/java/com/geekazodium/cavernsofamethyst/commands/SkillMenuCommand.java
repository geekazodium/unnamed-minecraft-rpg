package com.geekazodium.cavernsofamethyst.commands;

import com.geekazodium.cavernsofamethyst.players.menus.Menu;
import com.geekazodium.cavernsofamethyst.players.menus.PlayerSkillMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            Menu menu = new PlayerSkillMenu();
            return menu.open(player);
        }
        return false;
    }
}
