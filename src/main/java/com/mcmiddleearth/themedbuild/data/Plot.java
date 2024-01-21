package com.mcmiddleearth.themedbuild.data;

import com.mcmiddleearth.pluginutil.plotStoring.IStoragePlot;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Plot  implements IStoragePlot {

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

    public enum SignCorner {
        NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST;
    }

    public enum Vote {
        VOTE, STAR;
    }
}
