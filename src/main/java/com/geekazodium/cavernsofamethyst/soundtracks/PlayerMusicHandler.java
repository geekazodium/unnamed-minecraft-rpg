package com.geekazodium.cavernsofamethyst.soundtracks;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerMusicHandler {
    private SoundTrack soundTrack = new DefaultSoundTrack();
    private SoundTrack newTrack = null;
    private int blendPercent = 0;
    private final Player player;
    public PlayerMusicHandler(Player player) {
        this.player = player;
    }

    public void tick() {
        if(soundTrack != null){
            soundTrack.tick(player,10);
        }
    }
}
