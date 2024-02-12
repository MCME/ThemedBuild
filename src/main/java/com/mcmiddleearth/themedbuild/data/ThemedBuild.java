package com.mcmiddleearth.themedbuild.data;

import org.bukkit.Location;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ThemedBuild {

    private String name;

    private String rpName;

    private Location location;

    private boolean allowPlotClaiming, allowBuilding, allowHelping, allowVoting;

    private List<Plot> upperRow, lowerRow, upperWinnerRow, lowerWinnerRow;

    private boolean useSingleWinnerRow;

    private PlotModel plotModel;

    public String getName() {
        return name;
    }

    public Plot getPlot(UUID sender, int number) {
        return null;
    }

    public Collection<Plot> getOwnedPlots(UUID playerId) {
        return null;
    }

    public Plot getUnclaimedPlot() {
        return null;
    }

    public Collection<Plot> getBuildPlots(UUID helper) {
        return null;
    }

    public boolean isClaimingAllowed() {
        return allowPlotClaiming;
    }

    public boolean isBuildingAllowed() {
        return allowBuilding;
    }

    public boolean isHelpingAllowed() {
        return allowHelping;
    }

    public boolean isVotingAllowed() {
        return allowVoting;
    }

    public void setAllowBuilding(boolean allow) {
        allowBuilding = allow;
    }

    public void setAllowClaiming(boolean allow) {
        allowPlotClaiming = allow;
    }

    public void setAllowHelping(boolean allow) {
        allowHelping = allow;
    }

    public int getRank(Plot plot) {
        return 0;
    }

    public void setWinner(int rank) {

    }

    public boolean isWinner(Plot plot) {
        return false;
    }

    public void removeWinner(Plot plot) {

    }

    public void setURL(String url) {

    }

    public String getURL() {
        return null;
    }

    public Collection<Plot> getClaimedPlots() {
        return null;
    }

    public Location getWarpLocation() {
        return null;
    }

}
