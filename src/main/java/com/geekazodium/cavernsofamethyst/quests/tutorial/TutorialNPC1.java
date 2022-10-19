package com.geekazodium.cavernsofamethyst.quests.tutorial;

import com.geekazodium.cavernsofamethyst.GameTickHandler;
import com.geekazodium.cavernsofamethyst.npc.PlayerNPC;
import com.geekazodium.cavernsofamethyst.npc.QuestNPC;
import com.geekazodium.cavernsofamethyst.npc.WorldNPCHandler;
import com.geekazodium.cavernsofamethyst.quests.CutsceneHandler;
import com.geekazodium.cavernsofamethyst.quests.PlayerQuestDataUtil;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;

import static com.geekazodium.cavernsofamethyst.Main.overworld;

public class TutorialNPC1 extends QuestNPC {

    public static final String TUTORIAL_1 = "tutorial1";

    public TutorialNPC1(WorldNPCHandler handler) {
        super("geekazodium", new Location(overworld,-97.5,63,46.5),handler);
    }

    @Override
    public void interact(PlayerInteractEntityEvent event) {
        GameTickHandler.players.get(event.getPlayer()).setCutscene(new TutorialDialogue1(event.getPlayer()));
        PersistentDataContainer container = PlayerQuestDataUtil.getQuestData(event.getPlayer(), TUTORIAL_1);
        if(PlayerQuestDataUtil.getQuestProgress(container) == 0){
            PlayerQuestDataUtil.setQuestProgress(container,1);
        }
        PlayerQuestDataUtil.updateQuestData(event.getPlayer(),TUTORIAL_1,container);
    }

    @Override
    public void spawn() {
        super.spawn();
        disguise.setName("placeholder name");
    }
}
