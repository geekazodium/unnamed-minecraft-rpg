package com.geekazodium.cavernsofamethyst.players;

import com.geekazodium.cavernsofamethyst.Main;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandler;
import com.geekazodium.cavernsofamethyst.items.CustomItemHandlerRegistry;
import com.geekazodium.cavernsofamethyst.items.weapons.WeaponItemHandler;
import com.geekazodium.cavernsofamethyst.quests.CutsceneHandler;
import com.geekazodium.cavernsofamethyst.quests.PlayerQuestDataUtil;
import com.geekazodium.cavernsofamethyst.soundtracks.PlayerMusicHandler;
import com.geekazodium.cavernsofamethyst.util.EntityDamageUtil;
import com.geekazodium.cavernsofamethyst.util.NMS_accessor.NMSItemStackAccessor;
import com.geekazodium.cavernsofamethyst.util.RandomUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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
import static com.geekazodium.cavernsofamethyst.util.NMS_accessor.NMSItemStackAccessor.fromItemStack;
import static net.minecraft.world.item.ItemStack.EMPTY;
import static org.bukkit.inventory.EquipmentSlot.*;

public class PlayerHandler {//Todo: fix player skills not loading properly
    public static final NamespacedKey VISUAL_ONLY_KEY = new NamespacedKey(Main.getInstance(),"visual_only");
    public static final NamespacedKey PERSISTENT_HEALTH_MODIFIER = new NamespacedKey(Main.getInstance(),"persistent_health_modifier");
    public static final NamespacedKey PERSISTENT_MANA_MODIFIER = new NamespacedKey(Main.getInstance(),"persistent_mana_modifier");
    public static final NamespacedKey PERSISTENT_ATTACK_MODIFIER = new NamespacedKey(Main.getInstance(),"persistent_attack_modifier");
    public static final double SCALING = 4.47227191413d;
    private final Random random = new Random();
    private final FallDamageCheckTask playerFallCheckTask;
    private final Player player;
    public int timeUntilNextRegenTick = 0;
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
        player.getInventory().clear();
        resetPlayerStatChanges();
        resetPlayerQuestMarkers();
        player.setLevel(0);
        player.setExp(0);
        respawnPlayer();
        updateStats();
    }

    private void resetPlayerStatChanges() {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(PERSISTENT_HEALTH_MODIFIER,PersistentDataType.INTEGER,0);
        container.set(PERSISTENT_MANA_MODIFIER,PersistentDataType.INTEGER,0);
        container.set(PERSISTENT_ATTACK_MODIFIER,PersistentDataType.INTEGER,0);
    }

    public int getSkillPointsLeft(){//todo rename this.
        PersistentDataContainer container = player.getPersistentDataContainer();
        return level*2
                - container.getOrDefault(PERSISTENT_HEALTH_MODIFIER,PersistentDataType.INTEGER,0)
                - container.getOrDefault(PERSISTENT_MANA_MODIFIER,PersistentDataType.INTEGER,0)
                - container.getOrDefault(PERSISTENT_ATTACK_MODIFIER,PersistentDataType.INTEGER,0);
    }

    private void resetPlayerQuestMarkers(){
        PlayerQuestDataUtil.clearQuestData(player);
    }

    public void respawnPlayer(){
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(Main.overworld,-152,120,4, -90, 90));
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                40,
                1,
                false
        ));
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        Connection networkManager = serverPlayer.networkManager;
        Inventory inventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();
        sendInventoryUpdatePacket(networkManager, inventory);
        sendVisualOnlyWeaponHotbar(player.getInventory(), (WeaponItemHandler) getActiveItemHandler());
    }

    public CustomItemHandler getActiveItemHandler() {
        return CustomItemHandlerRegistry.get(getActiveItemStack());
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
    private boolean isWeaponActive;
    private boolean allowSendInventoryUpdatePacket;
    private ItemStack activeWeaponItemStack;
    public boolean isWeaponActive(){
        return isWeaponActive;
    }
    public boolean isAllowSendInventoryUpdatePacket(){
        return allowSendInventoryUpdatePacket;
    }
    public void setAllowSendInventoryUpdatePacket(boolean b){
        allowSendInventoryUpdatePacket = b;
    }
    public void lowerWeapon() {
        if(!isWeaponActive())return;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItem(0);
        if (item!=null&&item.getType().equals(Material.STRUCTURE_VOID)){
            playerInventory.remove(item);
        }
        isWeaponActive = false;
        setAllowSendInventoryUpdatePacket(true);
        player.playSound(player,Sound.ITEM_ARMOR_EQUIP_IRON,1,1);
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        Connection networkManager = serverPlayer.networkManager;
        Inventory inventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();
        sendInventoryUpdatePacket(networkManager, inventory);
    }

    private static void sendInventoryUpdatePacket(Connection networkManager, Inventory inventory) {
        NonNullList<net.minecraft.world.item.ItemStack> inv = NonNullList.create();
        inv.add(EMPTY);inv.add(EMPTY);inv.add(EMPTY);inv.add(EMPTY);inv.add(EMPTY);
        inv.add(inventory.getArmor(3));
        inv.add(inventory.getArmor(2));
        inv.add(inventory.getArmor(1));
        inv.add(inventory.getArmor(0));
        inv.addAll(inventory.getContents().subList(9,36));
        inv.addAll(inventory.getContents().subList(0,9));
        ClientboundContainerSetContentPacket packet = new ClientboundContainerSetContentPacket(0, 1, inv, EMPTY);
        networkManager.send(packet);
    }

    private void sendVisualOnlyItemPacket(int slot, NMSItemStackAccessor itemStack){
        Connection networkManager = ((CraftPlayer) player).getHandle().networkManager;
        ClientboundContainerSetSlotPacket packet = new ClientboundContainerSetSlotPacket(-2,0,slot,itemStack.getItemStack());
        networkManager.send(packet);
    }

    public void readyWeapon() {
        if(isWeaponActive())return;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInMainHand = playerInventory.getItemInMainHand();
        activeWeaponItemStack = itemInMainHand;
        WeaponItemHandler weaponItemHandler = (WeaponItemHandler) CustomItemHandlerRegistry.get(itemInMainHand);
        sendVisualOnlyWeaponHotbar(playerInventory, weaponItemHandler);
        player.playSound(player,Sound.ITEM_ARMOR_EQUIP_IRON,1,1);
        isWeaponActive = true;
        setAllowSendInventoryUpdatePacket(false);
        ItemStack item = playerInventory.getItem(0);
        if(item == null || item.getType().equals(Material.AIR)){
            playerInventory.setItem(0,new ItemStack(Material.STRUCTURE_VOID));
        }
    }

    public void sendVisualOnlyWeaponHotbar(PlayerInventory playerInventory, WeaponItemHandler weaponItemHandler) {
        NMSItemStackAccessor visualWeaponStack = fromItemStack(net.minecraft.world.item.ItemStack.fromBukkitCopy(activeWeaponItemStack));
        NMSItemStackAccessor ability = new NMSItemStackAccessor(DyeItem.byColor(DyeColor.BLUE),1);
        NMSItemStackAccessor empty = new NMSItemStackAccessor();
        sendVisualOnlyItemPacket(0,visualWeaponStack);
        for (int i = 0; i <7; i++) {
            assert weaponItemHandler != null;
            if (weaponItemHandler.getSpecialAction(i)==null){
                sendVisualOnlyItemPacket(i + 1, empty);
            }else {
                sendVisualOnlyItemPacket(i + 1, ability);
            }
        }
        NMSItemStackAccessor barrier = new NMSItemStackAccessor(Items.BARRIER);
        sendVisualOnlyItemPacket(8, barrier);
        playerInventory.setHeldItemSlot(0);
    }

    public void updateStats(){
        player.setHealthScaled(true);
        level = player.getLevel();
        PlayerInventory inventory = player.getInventory();
        PlayerStats stats = new PlayerStats(player);
        applyStatsFromItem(inventory.getItemInMainHand(),stats, HAND);
        applyStatsFromItem(inventory.getItem(HEAD),stats, HEAD);
        applyStatsFromItem(inventory.getItem(CHEST),stats,CHEST);
        applyStatsFromItem(inventory.getItem(LEGS),stats,LEGS);
        applyStatsFromItem(inventory.getItem(FEET),stats,FEET);
        maxMana = stats.maxMana;//Changed the order of code execution to hopefully fix the "0 mana" bug
        stats.updatePlayer(player);
        AttributeInstance maxHp = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        maxHp.setBaseValue(stats.maxHealth);
        player.setHealth(Math.min(maxHp.getValue(),player.getHealth()));
    }

    public ItemStack getActiveItemStack(){
        if(isWeaponActive)return activeWeaponItemStack;
        return player.getInventory().getItemInMainHand();
    }

    private void applyStatsFromItem(ItemStack item, PlayerStats stats, EquipmentSlot slot){
        CustomItemHandler itemHandler = CustomItemHandlerRegistry.get(item);
        if(itemHandler == null) {
            return;
        }
        itemHandler.applyItemBaseStats(stats,player,item,slot);
    }

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

    public void onFallEvent(EntityDamageEvent event) {
        playerFallCheckTask.onFall(event);
    }

    public boolean isOnCooldown() {
        return getAtkCooldown()>0;
    }

    private boolean duplicateRightClick = false;
    public boolean checkForDuplicateRightClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        duplicateRightClick = !duplicateRightClick;
        return !duplicateRightClick;
    }

    public void onWeaponHotKeyEvent(PlayerItemHeldEvent event) {
        if(!(CustomItemHandlerRegistry.get(activeWeaponItemStack) instanceof WeaponItemHandler itemHandler))return;
        int newSlot = event.getNewSlot();
        if (newSlot == 8) {
            lowerWeapon();
        } else if (newSlot == 0) {
            event.setCancelled(false);
        }else{
            itemHandler.useSpecialAction(event.getPlayer(),newSlot-1);
        }
    }

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
                if(player.getLocation().subtract(0,1,0).getBlock().isCollidable()){
                    cancel = false;
                    player.setFallDistance(0);
                }
            }
            //lastFallDistance = player.getFallDistance();
            handler.scheduleAction(this,1);
        }

        public void cancelFallDamage() {
            //player.setFallDistance(-Float.MAX_VALUE);
            cancel = true;
        }

        public void onFall(EntityDamageEvent event) {
            if(cancel) {
                event.setCancelled(true);
                cancel = false;
            }
        }
    }

    private void updatePlayerActionBar() {
        Component actionBar = Component.text("❤ HEALTH "+
                (int)player.getHealth()+"/"+
                (int) Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()
        );
        actionBar = actionBar.style(actionBar.style().color(TextColor.color(0xFF5555)));
        Component append = Component.text("  ❃ MANA " + (int)mana + "/" + maxMana);
        append = append.style(actionBar.style().color(TextColor.color(0x55FFFF)));
        actionBar = actionBar.append(append);
        player.sendActionBar(actionBar);
    }

    private void updateVolatileStats() {
        if(multiplierTimer>0)multiplierTimer--;
        else {
            lastHandler=null;
        }
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(EFFECTIVE_ATTACK_KEY,PersistentDataType.INTEGER,container.getOrDefault(BASE_ATTACK_KEY,PersistentDataType.INTEGER,0));
        if(timeUntilNextRegenTick>0){
            timeUntilNextRegenTick--;
            return;
        }
        timeUntilNextRegenTick = 20-1;
        if(mana<maxMana) {
            mana += Math.sqrt(maxMana)/ SCALING;
        }
        if(mana>maxMana){
            mana = maxMana;
        }
        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        player.setHealthScale((SCALING *Math.sqrt(maxHealth))-0.5);
        if(player.getHealth()<maxHealth) {
            player.setHealth(Math.min(player.getHealth()+ Math.sqrt(maxHealth)/ SCALING,maxHealth));
        }
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

    CustomItemHandler lastHandler;
    int lastId;
    int spellCostMultiplier=1;
    int multiplierTimer = 0;

    public boolean consumeMana(int i,CustomItemHandler handler,int id) {
        if(isRepeatedSpell(handler,id)){
            spellCostMultiplier++;
        }else{
            spellCostMultiplier=1;
        }
        int cost = i * Math.max(spellCostMultiplier, 1);
        if(mana>= cost){
            multiplierTimer = 60;
            mana -= cost;
            player.sendMessage("[-"+cost+"]mana");
            lastHandler=handler;
            lastId=id;
            return true;
        }
        if(isRepeatedSpell(handler,id)){
            spellCostMultiplier--;
        }
        return false;
    }

    private boolean isRepeatedSpell(CustomItemHandler handler, int id) {
        return handler == lastHandler && id == lastId;
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
        public PlayerStats(Player player){
            PersistentDataContainer container = player.getPersistentDataContainer();
            maxHealth = 5+container.getOrDefault(PERSISTENT_HEALTH_MODIFIER,PersistentDataType.INTEGER,0);
            baseAttack = 5+container.getOrDefault(PERSISTENT_ATTACK_MODIFIER,PersistentDataType.INTEGER,0);
            maxMana = 5+container.getOrDefault(PERSISTENT_MANA_MODIFIER,PersistentDataType.INTEGER,0);
        }

        public void updatePlayer(Player player) {
            PersistentDataContainer container = player.getPersistentDataContainer();
            container.set(EntityDamageUtil.MAX_HEALTH_KEY,PersistentDataType.INTEGER,maxHealth);
            container.set(EntityDamageUtil.BASE_ATTACK_KEY,PersistentDataType.INTEGER,baseAttack);
            container.set(EntityDamageUtil.MAX_MANA_KEY,PersistentDataType.INTEGER,maxMana);
        }
    }
}
