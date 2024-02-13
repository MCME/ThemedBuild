package com.mcmiddleearth.themedbuild.data;

import com.mcmiddleearth.themedbuild.exception.NoCurrentThemeException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class ThemedBuildManager {

    private static World world;

    private static ArrayList<ThemedBuild> themedBuilds;

    public static void createNewTheme(String themeName, String modelName) {
        //todo: Create paths and first plot.
    }

    public static void deleteCurrentTheme() {
        //todo: delete paths and plots
    }

    public static Plot getPlot(Location location) {
        if(!world.equals(location.getWorld())) {
            return null;
        }
        ThemedBuild theme = getThemedBuildAt(location);
        return theme.getPlot(location);
    }

    public static @Nonnull ThemedBuild getCurrentThemedBuild() {
        if(themedBuilds.size()==0) throw new NoCurrentThemeException();
        return themedBuilds.get(themedBuilds.size()-1);
    }

    public static ThemedBuild getThemedBuild(String themeName) {
        List<ThemedBuild> matches = getMatches(themeName);
        return (matches.size()==1?matches.get(0):null);
    }

    public static Collection<ThemedBuild> getThemedBuilds() {
        return themedBuilds;
    }
    public static boolean isAmbiguous(String themeName) {
        return getMatches(themeName).size()>1;
    }

    private static List<ThemedBuild> getMatches(String themeName) {
        return themedBuilds.stream().filter(theme -> theme.getName().toLowerCase().contains(themeName.toLowerCase()))
                                    .collect(Collectors.toList());
    }

    private static ThemedBuild getThemedBuildAt(Location location) {
        int min = 0;
        int max = themedBuilds.size()-1;
        int current = max;
        while(!themedBuilds.get(current).isInside(location)) {
            int lastCurrent = current;
            boolean upper = false;
            if(themedBuilds.get(current).getWarpLocation().getZ()<location.getZ()) {
                max = current;
            } else {
                upper = true;
                min = current;
            }
            current = (int)(min + (max-min) * ((double)location.getZ()-themedBuilds.get(min).getWarpLocation().getZ())
                    /(themedBuilds.get(max).getWarpLocation().getZ()-themedBuilds.get(min).getWarpLocation().getZ()));
            if(upper && current == lastCurrent) {
                current ++;
            } else if(!upper && current == lastCurrent) {
                current --;
            }
        }
        return themedBuilds.get(current);
    }
}
