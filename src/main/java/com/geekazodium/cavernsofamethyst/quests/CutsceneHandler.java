package com.geekazodium.cavernsofamethyst.quests;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class CutsceneHandler {
    protected Message[] dialogue;
    int currentMessage=0;
    protected final Player player;
    public CutsceneHandler(Player player){
        this.player = player;
    }
    public boolean next(){
        Message message = dialogue[currentMessage];
        return next(message.name, message.descriptor, message.message);
    }

    protected boolean next(String name, @Nullable String descriptor, String message){
        if(dialogue != null) {
            if (currentMessage >= dialogue.length) {
                return true;
            }
            int width = 50;
            player.sendMessage(formatName(name,width));
            if(descriptor != null) {
                player.sendMessage(formatDescriptor(descriptor,width));
            }
            player.sendMessage(formatMessage(message,width));
            if (currentMessage < dialogue.length) {
                currentMessage += 1;
            }
        }
        return true;
    }

    protected Component formatMessage(String message, int width) {//TODO add newline at width, add color for text
        return Component.text(message);
    }

    protected Component formatDescriptor(String descriptor, int width) {
        String padding = new String(new char[(width - descriptor.length()) / 2]).replace(String.valueOf((char)0)," ");
        return Component.text(padding + descriptor + padding);
    }

    protected Component formatName(String name,int width) {
        String padding = new String(new char[(width - name.length()) / 2]).replace((char)0,'_');
        return Component.text(padding + name + padding);
    }

    public boolean ended() {
        return !(currentMessage < dialogue.length);
    }

    public abstract void performFinishedAction();

    public static class Message{
        public String name;
        public String descriptor;
        public String message;
        public Message(String name, @Nullable String descriptor, String message){
            this.name = name;
            this.message = message;
            this.descriptor = descriptor;
        }
        public Message(String name, String message) {
            this(name,null,message);
        }
    }
}
