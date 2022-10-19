package com.geekazodium.cavernsofamethyst.soundtracks;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;

import java.util.List;

import static com.geekazodium.cavernsofamethyst.soundtracks.Pitch.*;

public class DefaultSoundTrack extends AbstractSoundTrack{
    public DefaultSoundTrack(){
        this.notes.put(10, List.of(
                new Note(Instrument.PIANO,0, org.bukkit.Note.Tone.C, Note.NFS.natural),
                new Note(Instrument.PIANO,1, org.bukkit.Note.Tone.F, Note.NFS.natural)
        ));
        this.notes.put(20, List.of(
                new Note(Instrument.PIANO,0, org.bukkit.Note.Tone.G, Note.NFS.natural)
        ));
        this.notes.put(30, List.of(
                new Note(Instrument.PIANO,1, org.bukkit.Note.Tone.C, Note.NFS.natural)
        ));
        this.length = 30;
    }
}
