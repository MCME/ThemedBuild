package com.mcmiddleearth.themedbuild.data;

import com.mcmiddleearth.themedbuild.exception.NoCurrentThemeException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ThemedBuildManager {

    private static List<ThemedBuild> themedBuilds;

    private static Set<PlotModel> models;

    private static boolean liveOrdering;

    public static @Nonnull ThemedBuild getCurrentThemedBuild() {
        if(themedBuilds.size()==0) throw new NoCurrentThemeException();
        return themedBuilds.get(themedBuilds.size()-1);
    }

    public static ThemedBuild getThemedBuild(String themeName) {
        return null;
    }

    public static Plot getPlot(Location location) {
        return null;
    }

    public static void createNewTheme(String themeName, String modelName) {

    }

    public static Collection<ThemedBuild> getThemedBuilds() {
        return themedBuilds;
    }
}
