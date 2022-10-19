package com.geekazodium.cavernsofamethyst;

import com.geekazodium.cavernsofamethyst.commands.DebugHitboxCommand;
import com.geekazodium.cavernsofamethyst.commands.GetItemCommand;
import com.geekazodium.cavernsofamethyst.items.Bows.WoodenBow;
import com.geekazodium.cavernsofamethyst.items.Swords.WarmBlade;
import com.geekazodium.cavernsofamethyst.items.Wands.Icicle;
import com.geekazodium.cavernsofamethyst.listeners.*;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

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
        registerListeners();
        registerCommandListeners();
        setGameSettings();
        new WarmBlade().register();
        new WoodenBow().register();
        new Icicle().register();
        tickHandler = new GameTickHandler();
        minecraftServer.getScheduler().scheduleSyncDelayedTask(this, tickHandler,1);
        LOGGER.log(Level.INFO,"caverns of amethyst has been successfully loaded");
        //minecraftServer.getScheduler().scheduleSyncDelayedTask(this,,1);
    }

    @Override
    public void onDisable() {
        tickHandler.onClose();
    }

    private void registerListeners(){
        minecraftServer.getPluginManager().registerEvents(new PlayerUpdateInventoryListener(),this);
        minecraftServer.getPluginManager().registerEvents(new AttackListener(),this);
        minecraftServer.getPluginManager().registerEvents(new PlayerJoinListener(),this);
        minecraftServer.getPluginManager().registerEvents(new EntityDeathListener(),this);
        minecraftServer.getPluginManager().registerEvents(new ChunkLoadingListener(),this);
        minecraftServer.getPluginManager().registerEvents(new EntityInteractListener(),this);
        minecraftServer.getPluginManager().registerEvents(new PlayerShiftListener(),this);
    }

    private void registerCommandListeners(){
        minecraftServer.getPluginCommand("getItem").setExecutor(new GetItemCommand());
        minecraftServer.getPluginCommand("debugHitbox").setExecutor(new DebugHitboxCommand());
        minecraftServer.getPluginCommand("generateDungeon").setExecutor(new DebugHitboxCommand());
    }
}
