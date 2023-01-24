package com.geekazodium.cavernsofamethyst.commands;

import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeSet;

public class GetItemListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> keySet = CustomItemHandlerRegistry.getKeySet().stream().toList();
        if(args.length >1)return false;
        int page = 0;
        if(args.length ==1) page = Integer.parseInt(args[0]);
        if(page<0)return false;
        if(page>keySet.size()/10+((keySet.size()%10>0)?1:0))return false;
        for (int i = Math.max(page*10,0); i < Math.min(page*10+10,keySet.size()); i++) {
            sender.sendMessage(formatItemNames(keySet.get(i)));
        }
        return true;
    }

    private static Component formatItemNames(String name){
        Component itemNameComponent = Component.text(name);
        itemNameComponent.style(
                Style.style().color(TextColor.color(0x9D2B))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,"/getitem "+name))
                        .build()
        );
        return itemNameComponent;
    }
}
