package org.fejzu.golem.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.fejzu.golem.Plugin;
import org.fejzu.golem.data.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectionsManager
{
    public final List<Collections> collectionsList = new ArrayList<>();

    public void loadCollections() {
        ConfigurationSection collectionsListFromConfig = Plugin.getInstance().getCollections().getConfig().getConfigurationSection("collections");
        for (String collectionsName : collectionsListFromConfig.getKeys(false)) {
            ConfigurationSection section = collectionsListFromConfig.getConfigurationSection(collectionsName);
            Collections collections = new Collections(
                    collectionsName);
            collectionsList.add(collections);
        }
    }

    public Optional<Collections> getCollections(String name) {
        return collectionsList.stream().filter(collections -> collections.getName().equalsIgnoreCase(name)).findAny();
    }

    public void reloadCollections() {
        collectionsList.clear();
        loadCollections();
    }
}
