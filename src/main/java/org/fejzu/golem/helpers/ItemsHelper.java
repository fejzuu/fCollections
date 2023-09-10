package org.fejzu.golem.helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemsHelper
{
    public static ItemStack getItemFromConfig(ConfigurationSection section) {
        ItemStack itemStack = new ItemStack(Material.valueOf(section.getString(".material").toUpperCase()));
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (section.getString("head-texture") != null) {
            SkullMeta meta = (SkullMeta)itemMeta;
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", section.getString("head-texture")));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
        itemMeta.setDisplayName(ChatHelper.fix(section.getString(".displayname")));
        List<String> lore = new ArrayList<>(section.getStringList(".lores"));
        itemMeta.setLore(ChatHelper.fix(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Inventory getItemFromConfigInventory(Inventory inventory, ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection bgItemSection = section.getConfigurationSection("." + key);
            ItemStack itemStack = getItemFromConfig(bgItemSection);
            List<Integer> slots = new ArrayList<>(bgItemSection.getIntegerList(".slots"));
            for (Iterator<Integer> iterator = slots.iterator(); iterator.hasNext(); ) {
                int slot = iterator.next().intValue();
                inventory.setItem(slot, itemStack);
            }
        }
        return inventory;
    }
}
