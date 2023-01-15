package com.geekazodium.cavernsofamethyst.util;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.quests.CutsceneHandler;
import com.geekazodium.cavernsofamethyst.quests.PlayerQuestDataUtil;
import com.geekazodium.cavernsofamethyst.soundtracks.PlayerMusicHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.geekazodium.cavernsofamethyst.listeners.EntityInteractListener.playerInteractNpcCooldown;
import static com.geekazodium.cavernsofamethyst.util.EntityDamageUtil.BASE_ATTACK_KEY;
import static com.geekazodium.cavernsofamethyst.util.EntityDamageUtil.EFFECTIVE_ATTACK_KEY;

public class PlayerHandler {
    public static final NamespacedKey VISUAL_ONLY_KEY = new NamespacedKey(Main.getInstance(),"visual_only");
    private final Random random = new Random();
    private final FallDamageCheckTask playerFallCheckTask;
    private final Player player;
    private int attackCooldown = 0;
    private int elementalCharge = 0;
    public float mana = 0;
    private int maxMana = 0;
    private int level;
    private final PlayerMusicHandler playerMusicHandler;
    private int hasSneaked = 0;
    private CutsceneHandler cutscene;
    private final Map<Runnable,Integer> scheduledTasks = new HashMap<>();

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
        this.playerFallCheckTask = new FallDamageCheckTask(player,this);
        scheduleAction(playerFallCheckTask,1);
        this.playerMusicHandler= new PlayerMusicHandler(player);
    }

    /*public void updateStats(){
        level = player.getLevel();
        PlayerInventory inventory = player.getInventory();
        PlayerStats stats = new PlayerStats();
        CustomItemHandler itemHandler = CustomItemHandlerRegistry.get(inventory.getItem(EquipmentSlot.HAND));
        if(itemHandler != null) {
            itemHandler.applyItemBaseStats(stats,player,itemHandler,player);
        }
        stats.updatePlayer(player);
        maxMana = stats.maxMana;
    }*/

    public void tick(){
        player.setFoodLevel(20);
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
        updateScheduledTasks();
        int cooldown = player.getPersistentDataContainer().getOrDefault(playerInteractNpcCooldown, PersistentDataType.INTEGER,0);
        updatePlayerActionBar();
        if(cooldown<=0){
            return;
        }
        player.getPersistentDataContainer().set(playerInteractNpcCooldown,PersistentDataType.INTEGER,cooldown -1);
    }

    private void updateScheduledTasks() {
        List<Runnable> toRun = new ArrayList<>();
        for (Runnable runnable:scheduledTasks.keySet()) {
            scheduledTasks.replace(runnable,scheduledTasks.get(runnable)-1);
        }
        scheduledTasks.forEach((runnable, integer) -> {
            if(integer<=0) {
                toRun.add(runnable);
            }
        });
        toRun.forEach(runnable -> {
            scheduledTasks.remove(runnable);
            runnable.run();
        });
    }

   /* public void onFallDamage(EntityDamageEvent event) {
        playerFallCheckTask.onFall(event);
    }*/

    private static class FallDamageCheckTask implements Runnable{
        private final Player player;
        private final PlayerHandler handler;
        //float lastFallDistance = 0;
        boolean cancel = false;
        public FallDamageCheckTask(Player player,PlayerHandler handler){
            this.player = player;
            this.handler = handler;
        }
        @Override
        public void run() {
            if(cancel) {
                if(!player.getLocation().subtract(0,1,0).getBlock().isCollidable()){
                        //!player.getLocation().subtract(0,2,0).getBlock().isCollidable()){
                    player.setFallDistance(0);
                }else{
                    cancel = false;
                }
            }
            //lastFallDistance = player.getFallDistance();
            handler.scheduleAction(this,1);
        }

        public void cancelFallDamage() {
            cancel = true;
        }

        /*public void onFall(EntityDamageEvent event) {
            event.setCancelled(cancel);
            cancel = false;
        }*/
    }

    private void updatePlayerActionBar() {
        Component actionBar = Component.text("hp:"+
                (int)player.getHealth()+"/"+
                (int) Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()
        );
        actionBar = actionBar.style(actionBar.style().color(TextColor.color(0xaf2000)));
        Component append = Component.text("mana:" + (int)mana + "/" + maxMana);
        append = append.style(actionBar.style().color(TextColor.color(0xACAF)));
        actionBar = actionBar.append(
                append
        );
        player.sendActionBar(actionBar);
    }

    private void updateVolatileStats() {
        PersistentDataContainer container = player.getPersistentDataContainer();
        if(mana<maxMana) {
            mana += maxMana/500f;
        }else if(mana>maxMana){
            mana = maxMana;
        }
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

    public void onLevelUpdate(int newLevel) {
        if(this.level >= newLevel){
            return;
        }
        this.level = newLevel;
        PlayLevelUpEffect();
    }

    private void PlayLevelUpEffect() {
        Location location = player.getEyeLocation();
        Firework firework = player.getWorld().spawn(location.add(1,0,0).add(RandomUtil.randomVec(0.5,random)), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.AQUA).build());
        firework.setFireworkMeta(fireworkMeta);
        firework.getPersistentDataContainer().set(VISUAL_ONLY_KEY,PersistentDataType.BYTE,(byte)1);
        firework = player.getWorld().spawn(location.add(-1,0,0).add(RandomUtil.randomVec(0.5,random)), Firework.class);
        fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.ORANGE).build());
        firework.setFireworkMeta(fireworkMeta);
        firework.getPersistentDataContainer().set(VISUAL_ONLY_KEY,PersistentDataType.BYTE,(byte)1);
        firework = player.getWorld().spawn(location.add(0,0,1).add(RandomUtil.randomVec(0.5,random)), Firework.class);
        fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.WHITE).build());
        firework.setFireworkMeta(fireworkMeta);
        firework.getPersistentDataContainer().set(VISUAL_ONLY_KEY,PersistentDataType.BYTE,(byte)1);
        firework = player.getWorld().spawn(location.add(0,0,-1).add(RandomUtil.randomVec(0.5,random)), Firework.class);
        fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.LIME).build());
        firework.setFireworkMeta(fireworkMeta);
        firework.getPersistentDataContainer().set(VISUAL_ONLY_KEY,PersistentDataType.BYTE,(byte)1);
    }

    public int elementalCharge() {
        return elementalCharge;
    }

    public void consumeElementalCharge(int elementalChargeReq) {
        elementalCharge -= elementalChargeReq;
        if(elementalCharge<0){
            elementalCharge = 0;
        }
    }

    public void increaseElementalCharge(int i) {
        elementalCharge += Math.max(0,i);
    }

    public boolean consumeMana(int i) {
        if(mana>=i){
            mana -= i;
            return true;
        }
        return false;
    }

    public void scheduleAction(Runnable runnable,int delay) {
        scheduledTasks.put(runnable,delay);
    }

    public Player getPlayer() {
        return player;
    }

    public void cancelNextFallDamage() {
        playerFallCheckTask.cancelFallDamage();
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
