package org.fejzu.golem;

import org.bukkit.plugin.java.JavaPlugin;
import org.fejzu.golem.commands.CollectionCommand;
import org.fejzu.golem.commands.CollectionsCommand;
import org.fejzu.golem.files.CollectionFile;
import org.fejzu.golem.files.InventoryFile;
import org.fejzu.golem.listeners.EntityInteractListener;
import org.fejzu.golem.listeners.InventoryClickListener;
import org.fejzu.golem.listeners.PlayerDeathListener;
import org.fejzu.golem.managers.CollectionsManager;
import org.fejzu.golem.tasks.RewardTask;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin {

    private static Plugin instance;
    private final Logger logger = getLogger();
    private CollectionFile collectionFile;
    private InventoryFile inventoryFile;
    private CollectionsManager collectionsManager;

    @Override
    public void onEnable() {
        initialize();
    }

    private void initialize() {
        long start = System.currentTimeMillis();
        logger.log(Level.INFO, "");
        logger.log(Level.INFO, " .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------. ");
        logger.log(Level.INFO, "| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |");
        logger.log(Level.INFO, "| |  _________   | || |    ______    | || |     ____     | || |   _____      | || |  _________   | || | ____    ____ | |");
        logger.log(Level.INFO, "| | |_   ___  |  | || |  .' ___  |   | || |   .'    `.   | || |  |_   _|     | || | |_   ___  |  | || ||_   \\  /   _|| |");
        logger.log(Level.INFO, "| |   | |_  \\_|  | || | / .'   \\_|   | || |  /  .--.  \\  | || |    | |       | || |   | |_  \\_|  | || |  |   \\/   |  | |");
        logger.log(Level.INFO, "| |   |  _|      | || | | |    ____  | || |  | |    | |  | || |    | |   _   | || |   |  _|  _   | || |  | |\\  /| |  | |");
        logger.log(Level.INFO, "| |  _| |_       | || | \\ `.___]  _| | || |  \\  `--'  /  | || |   _| |__/ |  | || |  _| |___/ |  | || | _| |_\\/_| |_ | |");
        logger.log(Level.INFO, "| | |_____|      | || |  `._____.'   | || |   `.____.'   | || |  |________|  | || | |_________|  | || ||_____||_____|| |");
        logger.log(Level.INFO, "| |              | || |              | || |              | || |              | || |              | || |              | |");
        logger.log(Level.INFO, "| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |");
        logger.log(Level.INFO, " '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------' ");
        logger.log(Level.INFO, "");
        logger.log(Level.INFO, "Version: " + getDescription().getVersion());
        logger.log(Level.INFO, "Author: fejzu");
        logger.log(Level.INFO, "");
        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException ex) {
            logger.severe("fVouchers requires Spigot to run, you can download");
            logger.severe("Spigot here: https://www.spigotmc.org/wiki/spigot-installation/.");
            getPluginLoader().disablePlugin(this);
            return;
        }
        instance = this;
        collectionFile = new CollectionFile();
        inventoryFile = new InventoryFile();
        collectionsManager = new CollectionsManager();
        saveDefaultConfig();
        collectionFile.loadFile();
        inventoryFile.loadFile();
        collectionsManager.loadCollections();
        new EntityInteractListener(this);
        new PlayerDeathListener(this);
        new RewardTask(this);
        new InventoryClickListener(this);
        new CollectionCommand(this);
        new CollectionsCommand(this);
        logger.log(Level.INFO, "Loaded " + collectionsManager.collectionsList.size() + " collections");
        logger.log(Level.INFO, "Successfully loaded in " + (System.currentTimeMillis() - start) + "ms");
    }

    public void reloadPlugin() {
        long start = System.currentTimeMillis();
        reloadConfig();
        collectionFile.loadFile();
        inventoryFile.loadFile();
        collectionsManager.reloadCollections();
        new CollectionsCommand(this);
        logger.info("file collections.yml has been reload and loaded: " + collectionsManager.collectionsList.size() + " collections");
        logger.info("file config.yml has been reloaded");
        logger.info("file inventory.yml has been reloaded");
        logger.log(Level.INFO, "Successfully reloaded plugin in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public CollectionFile getCollections() {
        return this.collectionFile;
    }

    public InventoryFile getInventoryFile() {
        return this.inventoryFile;
    }

    public CollectionsManager getCollectionsManager() {
        return this.collectionsManager;
    }
}