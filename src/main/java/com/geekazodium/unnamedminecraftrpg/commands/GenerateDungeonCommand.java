package com.geekazodium.unnamedminecraftrpg.commands;

import com.geekazodium.unnamedminecraftrpg.dungeons.Cavern1;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GenerateDungeonCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if (args[0].equals("1")) {
                (new Cavern1()).generate(player.getLocation());
            }
        }
        return false;
    }
}
