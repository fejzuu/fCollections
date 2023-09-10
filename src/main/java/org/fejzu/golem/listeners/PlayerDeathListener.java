package org.fejzu.golem.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.helpers.ChatHelper;

import java.util.Random;

public class PlayerDeathListener implements Listener
{
    public static final Random RANDOM_INSTANCE = new Random();

    public PlayerDeathListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getPlayer().getKiller();
        if (Plugin.getInstance().getConfig().getBoolean("shards_for_kills.enabled")) {
            if (getChance(Plugin.getInstance().getConfig().getDouble("shards_for_kills.chance"))) {
                String message = Plugin.getInstance().getConfig().getString("messages.shard_for_kill.message");
                message = StringUtils.replace(message, "{PLAYER}", event.getEntity().getName());
                ChatHelper.message(killer, Plugin.getInstance().getConfig().getString("messages.shard_for_kill.type"), message);
                killer.getInventory().addItem(Plugin.getInstance().getConfig().getItemStack("shard"));
            }
        }
    }

    public static boolean getChance(double chance) {
        return (chance >= 100.0D || chance >= getRandDouble(0.0D, 100.0D));
    }

    public static Double getRandDouble(double min, double max) throws IllegalArgumentException {
        return RANDOM_INSTANCE.nextDouble() * (max - min) + min;
    }
}