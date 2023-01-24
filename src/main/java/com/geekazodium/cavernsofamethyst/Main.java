package com.geekazodium.cavernsofamethyst;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.geekazodium.cavernsofamethyst.commands.*;
import com.geekazodium.cavernsofamethyst.items.armor.helmet.LeatherCap;
import com.geekazodium.cavernsofamethyst.items.weapons.bows.TripleShot;
import com.geekazodium.cavernsofamethyst.items.weapons.bows.WoodenBow;
import com.geekazodium.cavernsofamethyst.items.weapons.swords.WarmBlade;
import com.geekazodium.cavernsofamethyst.items.weapons.wands.Icicle;
import com.geekazodium.cavernsofamethyst.listeners.*;
import com.geekazodium.cavernsofamethyst.players.PlayerHandler;
import com.google.j2objc.annotations.ReflectionSupport;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Server minecraftServer;
    public static boolean debugHitbox = false;
    public static World overworld;
    private static JavaPlugin instance;
    public static Logger LOGGER;
    private GameTickHandler tickHandler;

    public static JavaPlugin getInstance(){
        return instance;
    }

    private static void setInstance(JavaPlugin instance) {
        Main.instance = instance;
    }

    private void setGameSettings(){
        overworld.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        overworld.setGameRule(GameRule.NATURAL_REGENERATION,false);
        overworld.setGameRule(GameRule.RANDOM_TICK_SPEED,0);
        overworld.setClearWeatherDuration(100);
        overworld.setGameRule(GameRule.DO_FIRE_TICK,false);
        overworld.setGameRule(GameRule.DO_MOB_SPAWNING,false);
    }

    @Override
    public void onEnable() {
        setInstance(this);
        minecraftServer = getServer();
        overworld = minecraftServer.getWorld(NamespacedKey.minecraft("overworld"));
        LOGGER = getLogger();
        registerListeners("com.geekazodium.cavernsofamethyst.listeners");
        registerCommandListeners();
        registerProtocolListeners();
        setGameSettings();
        new WarmBlade().register();
        new WoodenBow().register();
        new Icicle().register();
        new LeatherCap().register();
        new TripleShot().register();
        tickHandler = new GameTickHandler();
        minecraftServer.getScheduler().scheduleSyncDelayedTask(this, tickHandler,1);
        LOGGER.log(Level.INFO,"caverns of amethyst has been successfully loaded");
        //minecraftServer.getScheduler().scheduleSyncDelayedTask(this,,1);
    }

    @Override
    public void onDisable() {
        tickHandler.onClose();
    }

    private void registerListeners(String path){
        Reflections reflections = new Reflections(path);
        Set<Class<? extends Listener>> listeners = reflections.getSubTypesOf(Listener.class);
        for (Class<? extends Listener> listener : listeners) {
            try {
                minecraftServer.getPluginManager().registerEvents(listener.getDeclaredConstructor().newInstance(), this);
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    private void registerProtocolListeners(){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(event.getPlayer());
                if(playerHandler.isWeaponActive()){
                    event.setCancelled(true);
                    return;
                }
                //event.getPlayer().sendMessage(event.getPacket().toString());
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(event.getPlayer());
                if(playerHandler.isWeaponActive()){
                    event.setCancelled(true);
                    return;
                }
                //event.getPlayer().sendMessage(event.getPacket().toString());
                event.getPlayer().sendMessage(event.getPacket().getType().toString());
                ClientboundContainerSetContentPacket packet = (ClientboundContainerSetContentPacket) event.getPacket().getHandle();
                event.getPlayer().sendMessage(String.valueOf(packet.getContainerId()));
                event.getPlayer().sendMessage(String.valueOf(packet.getStateId()));
                event.getPlayer().sendMessage(String.valueOf(packet.getItems()));
                event.getPlayer().sendMessage(String.valueOf(packet.getCarriedItem()));
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_DATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PlayerHandler playerHandler = GameTickHandler.getPlayerHandler(event.getPlayer());
                if(playerHandler.isWeaponActive()){
                    event.setCancelled(true);
                    return;
                }
                //event.getPlayer().sendMessage(event.getPacket().toString());
            }
        });
    }

    private void registerCommandListeners(){
        minecraftServer.getPluginCommand("getItem").setExecutor(new GetItemCommand());
        minecraftServer.getPluginCommand("setSkin").setExecutor(new SetSkinCommand());
        minecraftServer.getPluginCommand("skillMenu").setExecutor(new SkillMenuCommand());
        minecraftServer.getPluginCommand("setMaxHealth").setExecutor(new SetHealthCommand());
        minecraftServer.getPluginCommand("resetCharacter").setExecutor(new ResetCharacterCommand());
        minecraftServer.getPluginCommand("debugHitbox").setExecutor(new DebugHitboxCommand());
        minecraftServer.getPluginCommand("generateDungeon").setExecutor(new DebugHitboxCommand());
    }
}
