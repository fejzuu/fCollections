package org.fejzu.golem.inventory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.helpers.ChatHelper;
import org.fejzu.golem.helpers.ItemsHelper;
import java.util.ArrayList;
import java.util.List;

public class CollectionsInventory {
    public static Inventory open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, Plugin.getInstance().getInventoryFile().getConfig().getInt("inv.size"), ChatHelper.fix(Plugin.getInstance().getInventoryFile().getConfig().getString("inv.title")));
        for (String collections : Plugin.getInstance().getCollections().getConfig().getConfigurationSection("collections").getKeys(false)) {
            ConfigurationSection collectionsSection = Plugin.getInstance().getCollections().getConfig().getConfigurationSection("collections." + collections);
            int slot = collectionsSection.getInt(".item.slot");
            int max = collectionsSection.getInt(".max");
            int paid = collectionsSection.getInt(".paid");
            ItemStack item = ItemsHelper.getItemFromConfig(collectionsSection.getConfigurationSection(".item"));
            ItemMeta itemMeta = item.getItemMeta();
            if(itemMeta.getLore()!=null) {
                List<String> lore = new ArrayList<>(itemMeta.getLore());
                for (int i = 0; i < lore.size(); i++) {
                    String start = Plugin.getInstance().getConfig().getString("progress_style.start");
                    String end = Plugin.getInstance().getConfig().getString("progress_style.end");
                    String symbol = Plugin.getInstance().getConfig().getString("progress_style.symbol");
                    String color_green = Plugin.getInstance().getConfig().getString("progress_style.colors.1");
                    String color_red = Plugin.getInstance().getConfig().getString("progress_style.colors.2");
                    lore.set(i, lore.get(i).replace("{PROGRESS}", ChatHelper.getProgressBar(paid, max, 10, symbol, color_green, color_red)));
                    lore.set(i, lore.get(i).replace("{MAX}", "" + max + ""));
                    lore.set(i, lore.get(i).replace("{START}", start));
                    lore.set(i, lore.get(i).replace("{END}", end));
                    lore.set(i, lore.get(i).replace("{PAID}", "" + paid + ""));
                }
                itemMeta.setLore(ChatHelper.fix(lore));
            }
            item.setItemMeta(itemMeta);
            inventory.setItem(slot, item);
        }
        ItemsHelper.getItemFromConfigInventory(inventory, Plugin.getInstance().getInventoryFile().getConfig().getConfigurationSection("inv.backgrounds"));
        player.openInventory(inventory);
        return inventory;
    }
}