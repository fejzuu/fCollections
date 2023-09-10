package org.fejzu.golem.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.inventory.CollectionsInventory;

public class EntityInteractListener implements Listener {
    public EntityInteractListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (Plugin.getInstance().getConfig().getBoolean("how_to_open_inventory.entity.enabled")) {
            Player player = event.getPlayer();
            String entityTypeString = Plugin.getInstance().getConfig().getString("how_to_open_inventory.entity.entity");
            EntityType entityType = EntityType.valueOf(entityTypeString);
            if (event.getRightClicked().getType() == entityType) {
                CollectionsInventory.open(player);
            }
        }
    }
}


