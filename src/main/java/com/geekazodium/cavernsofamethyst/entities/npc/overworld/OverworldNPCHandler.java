package com.geekazodium.cavernsofamethyst.entities.npc.overworld;

import com.geekazodium.cavernsofamethyst.entities.npc.WorldNPCHandler;
import com.geekazodium.cavernsofamethyst.quests.tutorial.TutorialNPC1;
import org.bukkit.Location;

import static com.geekazodium.cavernsofamethyst.Main.overworld;

public class OverworldNPCHandler extends WorldNPCHandler {
    public OverworldNPCHandler() {
        super(overworld);
        this.addNPC(new TutorialNPC1(this));
        this.addNPC(new BartenderNPC(new Location(overworld,-111,63,59),this));
    }
}
