package com.geekazodium.unnamedminecraftrpg.quests.tutorial;

import com.geekazodium.unnamedminecraftrpg.GameTickHandler;
import com.geekazodium.unnamedminecraftrpg.entities.npc.QuestNPC;
import com.geekazodium.unnamedminecraftrpg.entities.npc.WorldNPCHandler;
import com.geekazodium.unnamedminecraftrpg.quests.PlayerQuestDataUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;

import static com.geekazodium.unnamedminecraftrpg.Main.overworld;

public class TutorialNPC1 extends QuestNPC {
    public static NamespacedKey TUTORIAL_QUEST_KEY = PlayerQuestDataUtil.questNamespaceKey("tutorial");
    public TutorialNPC1(WorldNPCHandler handler) {
        super("geekazodium", new Location(overworld,-97.5,63,46.5),handler);
    }

    @Override
    public void interact(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer container = PlayerQuestDataUtil.getQuestData(player, TUTORIAL_QUEST_KEY);
        if(PlayerQuestDataUtil.getQuestProgress(container) == 1){
            player.sendMessage("you have completed this quest.");
            return;
        }
        GameTickHandler.players.get(player).setCutscene(new TutorialDialogue1(player));
        PlayerQuestDataUtil.updateQuestData(player, TUTORIAL_QUEST_KEY,container);
    }

    @Override
    public void spawn() {
        super.spawn();
        disguise.setName("placeholder name");
    }
}
