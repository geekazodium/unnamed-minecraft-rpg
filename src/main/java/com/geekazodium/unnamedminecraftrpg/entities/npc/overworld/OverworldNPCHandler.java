package com.geekazodium.unnamedminecraftrpg.entities.npc.overworld;

import com.geekazodium.unnamedminecraftrpg.entities.npc.WorldNPCHandler;
import com.geekazodium.unnamedminecraftrpg.quests.tutorial.TutorialNPC1;
import org.bukkit.Location;

import static com.geekazodium.unnamedminecraftrpg.Main.overworld;

public class OverworldNPCHandler extends WorldNPCHandler {//TODO make another more interesting quest
    public OverworldNPCHandler() {
        super(overworld);
        this.addNPC(new TutorialNPC1(this));
        this.addNPC(new BartenderNPC(new Location(overworld,-111,63,59),this));
    }
}
