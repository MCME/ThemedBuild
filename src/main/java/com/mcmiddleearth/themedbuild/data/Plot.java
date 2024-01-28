package com.mcmiddleearth.themedbuild.data;

import com.mcmiddleearth.pluginutil.plotStoring.IStoragePlot;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Plot  implements IStoragePlot {

    @Nullable private ThemedBuild themedbuild; //null for model demo plots

    private PlotModel model; //required for model demo plots

    private Location lowerCorner, upperCorner;

    private boolean limitedHeight;

    private boolean allowWeOwner, allowWeHelper;

    private UUID owner;

    private boolean allowHelper;

    private List<UUID> helper;

    private SignCorner signCorner;

    private Map<UUID,Vote> votes;

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Location getLowCorner() {
        return null;
    }

    @Override
    public Location getHighCorner() {
        return null;
    }

    @Override
    public boolean isInside(Location location) {
        return false;
    }

    public PlotModel getModel() {
        return model;
    }

    public void reset() {
    }

    public Location getWarpLocation() {
        return null;
    }

    public boolean isClaimed() {
        return false;
    }

    public void claim(UUID player) {
        
    }

    public UUID getOwner() {
        return null;
    }

    public void unclaim() {
    }

    public Collection<UUID> getHelper() {
        return null;
    }

    public void addHelper(UUID helper) {
        
    }

    public void removeHelper(UUID helper) {
        
    }

    public void commit(UUID newOwner) {
        
    }

    public void leave(UUID uniqueId) {
    }

    public enum SignCorner {
        NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST;
    }

    public enum Vote {
        VOTE, STAR;
    }
}
