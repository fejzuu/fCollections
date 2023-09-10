package org.fejzu.golem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.inventory.CollectionsInventory;

public class CollectionCommand implements CommandExecutor {
    public CollectionCommand(Plugin plugin) {
        plugin.getCommand("collection").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info("Player only use this command");
            return true;
        }
        Player player = (Player) sender;
        if (Plugin.getInstance().getConfig().getBoolean("how_to_open_inventory.command.enabled")) {
            CollectionsInventory.open(player);
        }
        return false;
    }
}