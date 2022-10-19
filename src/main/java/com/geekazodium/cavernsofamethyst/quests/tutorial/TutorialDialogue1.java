package com.geekazodium.cavernsofamethyst.quests.tutorial;

import com.geekazodium.cavernsofamethyst.quests.CutsceneHandler;
import org.bukkit.entity.Player;

public class TutorialDialogue1 extends CutsceneHandler {
    public TutorialDialogue1(Player player) {
        super(player);
        this.dialogue = new Message[]{
                new Message("placeholder", "not null", "hi there, I haven't seen you around here before, you must be new to this place"),
                new Message("placeholder", "well, what do you say? should I take you on a tour around here?")
        };
    }

    @Override
    public void performFinishedAction() {
        player.sendMessage("finished");
        player.giveExp(1000);
    }
}
