package org.fejzu.golem.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.helpers.*;
import org.fejzu.golem.inventory.CollectionsInventory;

import java.util.Arrays;

public class CollectionsCommand implements CommandExecutor {
    public CollectionsCommand(Plugin plugin) {
        plugin.getCommand("collections").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Plugin.getInstance().getConfig().getString("permission"))) {
            ChatHelper.message(sender, Plugin.getInstance().getConfig().getString("messages.no_permissions.type"), Plugin.getInstance().getConfig().getString("messages.no_permissions.message"));
            return true;
        }
        if (args.length == 0) {
            ChatHelper.message(sender, Plugin.getInstance().getConfig().getString("messages.correct_use.type"), Plugin.getInstance().getConfig().getString("messages.correct_use.message"));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatHelper.fix("&bCollections &8(&f" + Plugin.getInstance().getCollectionsManager().collectionsList.size() + "&8):"));
                Plugin.getInstance().getCollectionsManager().collectionsList.forEach(collections -> sender.sendMessage(ChatHelper.fix("&f " + collections.getName())));
                return false;
            } else if (args[0].equalsIgnoreCase("reload")) {
                Plugin.getInstance().reloadPlugin();
                return false;
            } else if (args[0].equalsIgnoreCase("setshard")) {
                if (!(sender instanceof Player)) {
                    Bukkit.getLogger().info("Player only use this command");
                    return true;
                }
                Player player = (Player) sender;
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if(itemStack == null) {
                    ChatHelper.message(sender, Plugin.getInstance().getConfig().getString("messages.you_dont_have_shard.type"), Plugin.getInstance().getConfig().getString("messages.you_dont_have_shard.message"));
                    return false;

                }
                itemStack.setAmount(1);
                Plugin.getInstance().getConfig().set("shard", itemStack);
                Plugin.getInstance().saveConfig();
                ChatHelper.message(sender, Plugin.getInstance().getConfig().getString("messages.set_shard.type"), Plugin.getInstance().getConfig().getString("messages.set_shard.message"));
                return false;
            } else if (args[0].equalsIgnoreCase("test")) {
                if (!(sender instanceof Player)) {
                    Bukkit.getLogger().info("Player only use this command");
                    return true;
                }
                Player player = (Player) sender;
                CollectionsInventory.open(player);
                return false;
            }
        } else if (args.length == 2) {
            FileConfiguration config = Plugin.getInstance().getCollections().getConfig();
            if (args[0].equalsIgnoreCase("delete")) {
                String name = args[1];
                config.set("collections." + name, null);
                String message = Plugin.getInstance().getConfig().getString("messages.delete_event.message");
                message = StringUtils.replace(message, "{EVENT}", name);
                ChatHelper.message(sender, Plugin.getInstance().getConfig().getString("messages.delete_event.type"), message);
                Plugin.getInstance().getCollections().save();
                Plugin.getInstance().getCollectionsManager().reloadCollections();
                return false;
            } else if (args[0].equalsIgnoreCase("create")) {
                String name = args[1];
                config.set("collections."+name+".name", name);
                config.set("collections."+name+".max", 500);
                config.set("collections."+name+".paid", 0);
                config.set("collections."+name+".commands", "Here add a command");
                config.set("collections."+name+".item.displayname", "&b&lYour Collection");
                config.set("collections."+name+".item.material", "DIRT");
                config.set("collections."+name+".item.slot", 13);
                config.set("collections."+name+".item.lores", Arrays.asList("", " &7After reaching the final destination, the entire server", " &7Receives &fHere your event &7for &fHere your time", "", " &7Required number of shardss&8: &f{MAX}", "", " &7In the collection&8: &f{PAID}&8/&f{MAX}", " &7Progress&8: {START}&r{PROGRESS}{END}", "", "&aClick here to add some shards!"));
                String message = Plugin.getInstance().getConfig().getString("messages.create_event.message");
                message = StringUtils.replace(message, "{EVENT}", name);
                ChatHelper.message(sender, Plugin.getInstance().getConfig().getString("messages.create_event.type"), message);
                Plugin.getInstance().getCollections().save();
                Plugin.getInstance().getCollectionsManager().reloadCollections();
                return false;
            }
        }
        return false;
    }
}