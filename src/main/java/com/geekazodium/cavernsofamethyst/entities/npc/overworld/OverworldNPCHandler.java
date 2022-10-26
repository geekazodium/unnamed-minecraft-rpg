package com.geekazodium.cavernsofamethyst.entities.npc.overworld;

import com.geekazodium.cavernsofamethyst.entities.npc.WorldNPCHandler;
import com.geekazodium.cavernsofamethyst.quests.tutorial.TutorialNPC1;

import static com.geekazodium.cavernsofamethyst.Main.overworld;

public class OverworldNPCHandler extends WorldNPCHandler {
    public OverworldNPCHandler() {
        super(overworld);
        this.addNPC(new TutorialNPC1(this));
    }
}
