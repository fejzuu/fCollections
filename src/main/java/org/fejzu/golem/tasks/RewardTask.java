package org.fejzu.golem.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.fejzu.golem.Plugin;

public class RewardTask extends BukkitRunnable {

    public RewardTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            FileConfiguration config = Plugin.getInstance().getCollections().getConfig();
            for (String collections : config.getConfigurationSection("collections").getKeys(false)) {
                ConfigurationSection collectionsSection = config.getConfigurationSection("collections." + collections);
                int max = collectionsSection.getInt(".max");
                int paid = collectionsSection.getInt(".paid");
                if (paid == max) {
                    collectionsSection.set(".paid", 0);
                    Plugin.getInstance().getCollections().save();
                    Bukkit.getScheduler().runTask(Plugin.getInstance(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), collectionsSection.getString(".commands")));
                }
            }
        });
    }
}
