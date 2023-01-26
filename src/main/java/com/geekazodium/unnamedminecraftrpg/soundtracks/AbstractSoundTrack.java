package com.geekazodium.unnamedminecraftrpg.soundtracks;

import org.bukkit.Instrument;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSoundTrack implements SoundTrack{//Need help
    protected Map<Integer, Collection<Note>> notes = new HashMap<>();
    int length = 0;
    int elapsedTicks = 0;
    @Override
    public void tick(Player player, int volume) {
        elapsedTicks += 1;
        if(notes.containsKey(elapsedTicks)){
            notes.get(elapsedTicks).forEach(note -> note.play(player));
        }
        if(elapsedTicks > length){
            elapsedTicks = 0;
        }
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    protected static class Note{
        Instrument instrument;
        org.bukkit.Note note;

        public Note(Instrument instrument, org.bukkit.Note note){
            this.instrument = instrument;
            this.note = note;
        }

        public Note(Instrument instrument, int octave, org.bukkit.Note.Tone tone, NFS nfs){
            this(instrument,
                    (nfs == NFS.flat)?org.bukkit.Note.flat(octave,tone):(nfs == NFS.natural)? org.bukkit.Note.natural(octave,tone): org.bukkit.Note.sharp(octave,tone)
            );
        }

        public void play(Player player){
            player.playNote(player.getLocation(), instrument,note);
        }

        public enum NFS{
            flat(0),
            natural(1),
            sharp(2);

            private final int n;
            NFS(int n){
                this.n = n;
            }

            public int id(){
                return n;
            }
        }
    }
}
