package com.geekazodium.unnamedminecraftrpg.players.skills;

import com.geekazodium.unnamedminecraftrpg.util.menus.AbstractButtonMenu;
import net.kyori.adventure.text.Component;

import static com.geekazodium.unnamedminecraftrpg.players.PlayerHandler.*;

public class PlayerSkillMenu extends AbstractButtonMenu {
    public PlayerSkillMenu(){
        title = Component.text("player skills");
        rows = 3;
        //this.buttons.put(26,new CommandButton("stop"));
        this.buttons.put(14,new PlayerSkillModifyButton(PERSISTENT_ATTACK_MODIFIER));
        this.buttons.put(15,new PlayerSkillModifyButton(PERSISTENT_MANA_MODIFIER));
        this.buttons.put(16,new PlayerSkillModifyButton(PERSISTENT_HEALTH_MODIFIER));
    }
}
