package com.mcmiddleearth.themedbuild.data;

import org.bukkit.Location;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ThemedBuild {

    private String name;

    private String[] resourcePacks;

    private String url;

    private int orderingPeriod;

    private boolean limitedHeight;

    private boolean allowPlotClaiming, allowBuilding, allowHelping, allowVoting, allowWeOwner, allowWeHelper;

    private Location warpLocation, lowerCorner, upperCorner;

    private List<Plot> generalPlots, winnerPlots, unclaimedPlots;

    private boolean useSingleWinnerRow;

    private PlotModel plotModel;

    public Plot getUnclaimedPlot() {
        if(unclaimedPlots.size()==0) {
            //todo: create new plot
        }
        return unclaimedPlots.get(0);
    }

    public void setWinner(Plot plot, int rank) {
        plot.setRank(rank);
        //todo: move plot to winner side
    }

    public void removeWinner(Plot plot) {
        plot.setRank(0);
        //todo: move plot to general side
    }

    public Plot getPlot(UUID sender, int number) {
        //todo: decide about this method what to do.
        return null;
    }

    public Plot getPlot(Location location) {
        //todo: find plot at location
    }

    public Collection<Plot> getBuildPlots(UUID helper) {
        return getClaimedPlots().stream().filter(plot->plot.getHelpers().contains(helper)).collect(Collectors.toList());
    }

    public Collection<Plot> getOwnedPlots(UUID playerId) {
        return getClaimedPlots().stream().filter(plot->plot.getOwner().equals(playerId)).collect(Collectors.toList());
    }

    public List<Plot> getAllPlots() {
        List<Plot> result = new LinkedList<>(generalPlots);
        result.addAll(winnerPlots);
        return result;
    }

    public Collection<Plot> getClaimedPlots() {
        List<Plot> result = getAllPlots();
        result.removeAll(unclaimedPlots);
        return result;
    }

    public void claim(Plot plot, UUID player) {
        if(!plot.isClaimed()) {
            plot.claim(player);
            unclaimedPlots.remove(plot);
        }
    }

    public void unclaim(Plot plot) {
        if(plot.isClaimed()) {
            plot.unclaim();
            unclaimedPlots.add(plot);
        }
    }

    public String getName() {
        return name;
    }

    public void setRP(String[] resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public String[] getResourcePacks() {
        return resourcePacks;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }

    public Location getWarpLocation() {
        return warpLocation.clone();
    }

    public Location getLowerCorner() {
        return lowerCorner.clone();
    }

    public Location getUpperCorner() {
        return upperCorner.clone();
    }

    public boolean isInside(Location location) {
        return lowerCorner.getX()<location.getX() && lowerCorner.getZ()<location.getZ()
            && upperCorner.getX()>location.getX() && upperCorner.getZ()>location.getZ();
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

    public void setAllowVoting(Boolean allow) {
        allowVoting = allow;
    }

}
