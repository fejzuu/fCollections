package org.fejzu.golem.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.data.Collections;
import org.fejzu.golem.helpers.ChatHelper;
import org.fejzu.golem.inventory.CollectionsInventory;

import java.util.Optional;

public class InventoryClickListener implements Listener
{
    public InventoryClickListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvnetoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        for (String collectionsList : Plugin.getInstance().getCollections().getConfig().getConfigurationSection("collections").getKeys(false)) {
            ConfigurationSection collectionsSection = Plugin.getInstance().getCollections().getConfig().getConfigurationSection("collections." + collectionsList);
            int slot = collectionsSection.getInt(".item.slot");
            int max = collectionsSection.getInt(".max");
            int paid = collectionsSection.getInt(".paid");
            ItemStack itemStack = Plugin.getInstance().getConfig().getItemStack("shard");
            if (event.getView().getTitle().equalsIgnoreCase(ChatHelper.fix(Plugin.getInstance().getInventoryFile().getConfig().getString("inv.title")))) {
                event.setCancelled(true);
                if (event.getSlot() == slot) {
                    if (paid < max) {
                        if (itemStack == null) {
                            player.closeInventory();
                            player.sendMessage(ChatHelper.fix("&cAdmin needs to set the shard!"));
                            return;
                        }
                        int calcItem = calcItem(player, itemStack);
                        if (calcItem <= 0) {
                            player.closeInventory();
                            String message = Plugin.getInstance().getConfig().getString("messages.you_dont_have_shard.message");
                            ChatHelper.message(player, Plugin.getInstance().getConfig().getString("messages.you_dont_have_shard.type"), message);
                            return;
                        }
                        removeItem(player, itemStack, 1);
                        collectionsSection.set(".paid", collectionsSection.getInt(".paid") + 1);
                        String message = Plugin.getInstance().getConfig().getString("messages.add_shard_to_event.message");
                        message = StringUtils.replace(message, "{EVENT}", collectionsSection.getName());
                        ChatHelper.message(player, Plugin.getInstance().getConfig().getString("messages.add_shard_to_event.type"), message);
                        Plugin.getInstance().getCollections().save();
                        CollectionsInventory.open(player);
                        return;
                    }
                    player.closeInventory();
                    String message = Plugin.getInstance().getConfig().getString("messages.event_is_full.message");
                    message = StringUtils.replace(message, "{MAX}", "" + max);
                    message = StringUtils.replace(message, "{PAID}", "" + paid);
                    ChatHelper.message(player, Plugin.getInstance().getConfig().getString("messages.event_is_full.type"), message);
                    return;
                }
            }
        }
    }

    public static void removeItem(Player player, ItemStack fromItem, int amount) {
        int removeItem = amount;
        for(ItemStack itemStack : player.getInventory()) {
            if(itemStack==null || itemStack.getType()== Material.AIR) {
                continue;
            }
            if(itemStack.isSimilar(fromItem)) {
                if(removeItem<=0) {
                    break;
                }
                if(itemStack.getAmount()<=removeItem) {
                    removeItem -= itemStack.getAmount();
                    itemStack.setAmount(0);
                }
                else {
                    itemStack.setAmount(itemStack.getAmount()-removeItem);
                    removeItem=0;
                }
            }
        }
    }

    public static int calcItem(Player player, ItemStack fromItem) {
        int amount = 0;
        for(ItemStack itemStack : player.getInventory()) {
            if(itemStack==null || itemStack.getType()== Material.AIR) {
                continue;
            }
            if(itemStack.isSimilar(fromItem)) {
                amount += itemStack.getAmount();
            }
        }
        return amount;
    }
}
