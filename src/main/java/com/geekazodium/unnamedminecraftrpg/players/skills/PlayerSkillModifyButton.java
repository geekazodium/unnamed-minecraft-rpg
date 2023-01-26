package com.geekazodium.unnamedminecraftrpg.players.skills;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.players.PlayerHandler;
import com.geekazodium.unnamedminecraftrpg.util.menus.buttons.AbstractDynamicButton;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerSkillModifyButton extends AbstractDynamicButton {
    private final NamespacedKey statKey;
    public PlayerSkillModifyButton(NamespacedKey key){
        statKey = key;
    }
    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getViewers().get(0) instanceof Player player){
            PlayerHandler handler = GameTickHandler.getPlayerHandler(player);
            PersistentDataContainer container = player.getPersistentDataContainer();
            boolean increase = event.isLeftClick();
            boolean extra = event.isShiftClick();
            int n = container.getOrDefault(statKey,PersistentDataType.INTEGER,0);
            int pointsLeft = handler.getSkillPointsLeft();
            if(increase){
                int v = Math.min(pointsLeft, (extra) ? 5 : 1);
                n+= v;
            }else{
                if(n>0){
                    int s = Math.min(n, (extra) ? 5 : 1);
                    n-=s;
                }
            }
            container.set(statKey,PersistentDataType.INTEGER,n);
            handler.updateStats();
        }
    }

    @Override
    public ItemStack item(PlayerHandler handler) {
        PersistentDataContainer container = handler.getPlayer().getPersistentDataContainer();
        Integer stat = container.getOrDefault(statKey, PersistentDataType.INTEGER, 0);
        ItemStack itemStack = new ItemStack(Material.BOOK, Math.max(stat, 1));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text(statKey.toString()));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
