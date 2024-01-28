package com.mcmiddleearth.themedbuild.data;

import com.mcmiddleearth.themedbuild.domain.Plot;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class ThemedBuild {

    private String name;

    private String rpName;

    private Location location;

    private boolean allowPlotClaiming, allowBuilding;

    private List<Plot> upperRow, lowerRow, upperWinnerRow, lowerWinnerRow;

    private boolean useSingleWinnerRow;

    private PlotModel plotModel;

    public String getName() {
        return name;
    }

    public Plot getPlot(UUID sender, int number) {
        return null;
    }
}
