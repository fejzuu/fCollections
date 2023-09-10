package org.fejzu.golem.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.fejzu.golem.Plugin;

import java.io.File;
import java.io.IOException;

public class CollectionFile {
    private static File file;

    private static FileConfiguration config;

    public void setup() {
        file = new File(Plugin.getInstance().getDataFolder(), "collections.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Plugin.getInstance().saveResource("collections.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("Bro collections.yml is not working!");
        }
    }

    public void loadFile() {
        setup();
        save();
    }
}
