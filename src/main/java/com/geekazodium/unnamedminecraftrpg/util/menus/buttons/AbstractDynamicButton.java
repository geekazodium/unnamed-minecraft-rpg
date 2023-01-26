package com.geekazodium.unnamedminecraftrpg.util.menus.buttons;

import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractDynamicButton extends AbstractMenuButton {

    @Override
    public abstract ItemStack item(PlayerHandler handler);
}
