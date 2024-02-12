package com.mcmiddleearth.themedbuild;

import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class ThemedBuildConfig {

    private static final ConfigurationSection config = ThemedBuildPlugin.getPluginInstance().getConfig();

    public static int getMaxOwnedPlotsPerTheme(UUID playerId) {
        return 0;
    }

    public static int getMaxBuildPlotsPerTheme(UUID helper) {
        return 0;
    }


    public static int getPowerVoteFactor() {
        return config.getInt("voting.powerVoteFactor",2);
    }
}
