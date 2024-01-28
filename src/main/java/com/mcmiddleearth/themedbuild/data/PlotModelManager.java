package com.mcmiddleearth.themedbuild.data;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlotModelManager {

    public static boolean existsModel(String modelName) {
        return false;
    }

    public static PlotModel getModel(String modelName) {
        return null;
    }

    public static Plot getPlot(Location location) {
        return null;
    }

    public static boolean inPlot(Location location) {
        return false;
    }

    public static Plot getPlot(String modelName) {
        return null;
    }

    public static void addModel(Player player, Region region, String model, String description) {

    }

    public static void deleteModel(String modelName) {

    }

    public static Collection<PlotModel> getModels() {
        return new HashSet<PlotModel>();
    }
}
