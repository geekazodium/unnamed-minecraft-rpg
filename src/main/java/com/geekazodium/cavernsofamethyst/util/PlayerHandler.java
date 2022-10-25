package com.geekazodium.cavernsofamethyst.util;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.quests.CutsceneHandler;
import com.geekazodium.cavernsofamethyst.quests.PlayerQuestDataUtil;
import com.geekazodium.cavernsofamethyst.soundtracks.PlayerMusicHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static com.geekazodium.cavernsofamethyst.listeners.EntityInteractListener.playerInteractNpcCooldown;
import static com.geekazodium.cavernsofamethyst.util.EntityDamageUtil.BASE_ATTACK_KEY;
import static com.geekazodium.cavernsofamethyst.util.EntityDamageUtil.EFFECTIVE_ATTACK_KEY;

public class PlayerHandler {
    private final Player player;
    private int attackCooldown = 0;
    private int level;
    private final PlayerMusicHandler playerMusicHandler;
    private int hasSneaked = 0;
    private CutsceneHandler cutscene;

    public void onSneaked(){
        hasSneaked+=1;
    }

    public void initPlayer(){
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        respawnPlayer();
        resetPlayerQuestMarkers();
    }

    private void resetPlayerQuestMarkers(){
        PlayerQuestDataUtil.clearQuestData(player);
    }

    public void respawnPlayer(){
        player.teleport(new Location(Main.overworld,-152,120,4, -90, 90));
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                40,
                1,
                false
        ));
    }
    public boolean hasSneaked(){
        if(hasSneaked>0){
            hasSneaked -= 1;
            return true;
        }
        return false;
    }

    public PlayerHandler(Player player){
        this.player = player;
        this.playerMusicHandler= new PlayerMusicHandler(player);
    }

    public void updateStats(){
        level = player.getLevel();
        PlayerInventory inventory = player.getInventory();
        PlayerStats stats = new PlayerStats();
        CustomItemHandler itemHandler = CustomItemHandlerRegistry.get(inventory.getItem(EquipmentSlot.HAND));
        if(itemHandler != null) {
            itemHandler.applyItemBaseStats(stats,player);
        }
        stats.updatePlayer(player);
    }

    public void tick(){
        updateVolatileStats();
        attackCooldown -= 1;
        attackCooldown = Math.max(0,attackCooldown);
        //playerMusicHandler.tick();
        AttributeInstance atkSpeed = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED));
        atkSpeed.setBaseValue(attackCooldown>0?0:80);
        atkSpeed.getModifiers().forEach(atkSpeed::removeModifier);
        Material material = player.getInventory().getItemInMainHand().getType();
        if(player.getCooldown(material)<=0) {
            player.setCooldown(material,Math.max(0,attackCooldown));
        }
        while (hasSneaked()){
            if(cutscene != null){
                if (cutscene.ended()) {
                    cutscene.performFinishedAction();
                    cutscene = null;
                }else {
                    cutscene.next();
                }
            }
        }
        int cooldown = player.getPersistentDataContainer().getOrDefault(playerInteractNpcCooldown, PersistentDataType.INTEGER,0);
        if(cooldown<=0){
            return;
        }
        player.getPersistentDataContainer().set(playerInteractNpcCooldown,PersistentDataType.INTEGER,cooldown -1);
    }

    private void updateVolatileStats() {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(EFFECTIVE_ATTACK_KEY,PersistentDataType.INTEGER,container.getOrDefault(BASE_ATTACK_KEY,PersistentDataType.INTEGER,0));
    }

    public void setCutscene(CutsceneHandler handler) {
        if(this.cutscene == null){
            this.cutscene = handler;
            this.cutscene.next();
        }
    }

    public void setAtkCooldown(int delay) {
        attackCooldown = delay;
    }

    public int getAtkCooldown() {
        return attackCooldown;
    }

    public static class PlayerStats{
        public int maxHealth;
        public int baseAttack;
        public int maxMana;
        public PlayerStats(){
            maxHealth = 5;
            baseAttack = 5;
            maxMana = 5;
        }

        public void updatePlayer(Player player) {
            PersistentDataContainer container = player.getPersistentDataContainer();
            container.set(EntityDamageUtil.MAX_HEALTH_KEY,PersistentDataType.INTEGER,maxHealth);
            container.set(EntityDamageUtil.BASE_ATTACK_KEY,PersistentDataType.INTEGER,baseAttack);
            container.set(EntityDamageUtil.MAX_MANA_KEY,PersistentDataType.INTEGER,maxMana);
        }
    }
}
