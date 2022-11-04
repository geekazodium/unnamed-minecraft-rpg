package com.geekazodium.cavernsofamethyst.items.armor;

import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.UnstackableCustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class AbstractArmorItemHandler extends UnstackableCustomItem {
    protected AbstractArmorItemHandler(int newestVer, String id) {
        super(newestVer, id);
    }
}
