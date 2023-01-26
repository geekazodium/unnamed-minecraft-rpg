package com.geekazodium.unnamedminecraftrpg.quests.tutorial;

import com.geekazodium.unnamedminecraftrpg.quests.CutsceneHandler;
import com.geekazodium.unnamedminecraftrpg.quests.PlayerQuestDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import static com.geekazodium.unnamedminecraftrpg.quests.tutorial.TutorialNPC1.TUTORIAL_QUEST_KEY;

//import static com.geekazodium.cavernsofamethyst.quests.tutorial.TutorialNPC1.TUTORIAL_1;

public class TutorialDialogue1 extends CutsceneHandler {

    //public static NamespacedKey TUTORIAL_QUEST_KEY = PlayerQuestDataUtil.questNamespaceKey("tutorial1");
    public TutorialDialogue1(Player player) {
        super(player);
        this.dialogue = new Message[]{
                new Message("placeholder", "tavern owner", "hi there, I haven't seen you around here before, you must be new to this place"),
                new Message("placeholder", "well, what do you say? should I take you on a tour around here?")
        };
    }

    @Override
    public void performFinishedAction() {
        PersistentDataContainer container = PlayerQuestDataUtil.getQuestData(player, TUTORIAL_QUEST_KEY);
        PlayerQuestDataUtil.setQuestProgress(container,1);
        PlayerQuestDataUtil.updateQuestData(player, TUTORIAL_QUEST_KEY,container);
        //player.sendMessage("finished");
        player.giveExp(10);
    }
}
