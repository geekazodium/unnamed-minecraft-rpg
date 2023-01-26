package com.geekazodium.unnamedminecraftrpg.commands;

import com.geekazodium.unnamedminecraftrpg.items.CustomItemHandlerRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            player.getInventory().addItem(CustomItemHandlerRegistry.getFromId(args[0]).getNewestItem());
        }
        return false;
    }
}
