package com.geekazodium.unnamedminecraftrpg.commands;

import com.geekazodium.unnamedminecraftrpg.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DebugHitboxCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Main.debugHitbox = !Main.debugHitbox;
        return false;
    }
}
