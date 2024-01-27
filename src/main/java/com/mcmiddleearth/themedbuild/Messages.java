package com.mcmiddleearth.themedbuild;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class Messages {

    private static YamlConfiguration config;

    static{
        try {
            config = new YamlConfiguration();
            config.load(new File(ThemedBuildPlugin.getPluginInstance().getDataFolder(),"messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static @Nonnull String get(String node, String... replacements) {
        if(config.contains(node)) {
            String message = config.getString(node);
            assert message != null;
            for(int i = 0; i < replacements.length; i++) {
                message = message.replace("%"+i+"%", replacements[i]);
            }
        }
        return "Error! Message is missing in config file: "+node;
    }
}
