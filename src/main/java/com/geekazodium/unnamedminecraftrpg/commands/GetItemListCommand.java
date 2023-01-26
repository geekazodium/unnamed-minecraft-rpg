package com.geekazodium.unnamedminecraftrpg.commands;

import com.geekazodium.unnamedminecraftrpg.items.CustomItemHandlerRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetItemListCommand implements CommandExecutor {

    public static final @NotNull TextColor MENU_TITLE_COLOR = TextColor.color(0xEDEB8B);
    public static final @NotNull TextColor STRING_COLOR = TextColor.color(0x9D2B);
    public static final @NotNull TextColor MENU_ENABLED_COLOR = TextColor.color(0x1311C3);
    public static final @NotNull TextColor DIVIDER_COLOR = TextColor.color(0x00);
    public static final @NotNull TextColor MENU_DISABLED_COLOR = TextColor.color(0x00);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> keySet = CustomItemHandlerRegistry.getKeySet().stream().toList();
        if(args.length >1)return false;
        int page = 0;
        if(args.length ==1) page = Integer.parseInt(args[0]);
        if(page<0)return false;
        int totalPages = getTotalPages(keySet);
        if(page> totalPages)return false;
        Component[] padding = formatPadding(page,totalPages);
        for (Component line: padding) {
            sender.sendMessage(line);
        }
        for (int i = Math.max(page*10,0); i < Math.min(page*10+10,keySet.size()); i++) {
            sender.sendMessage(formatItemNames(keySet.get(i)));
        }
        sender.sendMessage(padding[1]);
        return true;
    }

    private static int getTotalPages(List<String> keySet) {
        return keySet.size() / 10 + ((keySet.size() % 10 > 0) ? 1 : 0)-1;
    }

    private Component[] formatPadding(int page, int totalPages) {
        Component[] headerComponents = new Component[]{
            Component.text("CustomItems"),
            Component.text("page "+page)
        };
        headerComponents[0]=Component.text("\t\t\t").append(headerComponents[0].style(
                Style.style()
                        .color(MENU_TITLE_COLOR)
                        .build()
        ));
        String divider = "---------------";
        headerComponents[1]=Component.text("<<<").style(
                (page>=1)?
                        Style.style()
                                .color(MENU_ENABLED_COLOR)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,"/getitemlist "+(page-1)))
                                .hoverEvent(HoverEvent.showText(Component.text("page "+(page-1))))
                                .build()
                        :
                        Style.style()
                                .color(MENU_DISABLED_COLOR)
                                .build()
        ).append(Component.text(divider).style(Style.style().color(DIVIDER_COLOR).clickEvent(null).hoverEvent(null).build())).append(headerComponents[1].style(
                Style.style()
                        .decorate(TextDecoration.BOLD)
                        .color(MENU_TITLE_COLOR)
                        .build()
        )).append(Component.text(divider).style(Style.style().color(DIVIDER_COLOR).build())).append(Component.text(">>>").style(
                (page+1<=totalPages)?
                        Style.style()
                                .color(MENU_ENABLED_COLOR)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,"/getitemlist "+(page+1)))
                                .hoverEvent(HoverEvent.showText(Component.text("page "+(page+1))))
                                .build()
                        :
                        Style.style()
                                .color(MENU_DISABLED_COLOR)
                                .build()
        ));
        return headerComponents;
    }

    private static Component formatItemNames(String name){
        Component itemNameComponent = Component.text(name);
        return itemNameComponent.style(
                Style.style().color(STRING_COLOR)
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,"/getitem "+name))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to get item "+name)))
                        .build()
        );
    }
}
