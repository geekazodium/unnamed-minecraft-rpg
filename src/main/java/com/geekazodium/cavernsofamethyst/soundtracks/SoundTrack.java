package com.geekazodium.cavernsofamethyst.soundtracks;

import org.bukkit.entity.Player;

public interface SoundTrack { //incomplete
    void tick(Player player,int volume);

    boolean isComplete();
}
