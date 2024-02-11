package com.mcmiddleearth.themedbuild.command.handler.executor;

import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.PlotModelManager;
import com.mcmiddleearth.themedbuild.data.ThemedBuild;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ConditionalExecutor {

    private List<Condition> conditions;

    private Player player;

    public ConditionalExecutor(Player player) {
        conditions = new LinkedList<>();
        this.player = player;
    }

    public ConditionalExecutor addCondition(boolean fulfilled, String errorMessage, String... messageArguments) {
        conditions.add(new Condition(fulfilled, errorMessage, messageArguments));
        return this;
    }

    public ConditionalExecutor addPlotCondition(Plot plot) {
       conditions.add(new Condition(plot!=null, "command.error.noPlot"));
       return this;
    }

    public ConditionalExecutor addPlotClaimedCondition(Plot plot) {
        conditions.add(new Condition(plot.isClaimed(), "command.error.unclaimed"));
        return this;
    }

    public ConditionalExecutor addPlotOwnedCondition(Plot plot, UUID ownerUuid) {
        conditions.add(new Condition(plot.getOwner().equals(ownerUuid), "command.error.notOwned"));
        return this;
    }

    public ConditionalExecutor addPlotHelperCondition(Plot plot, UUID helperUuid, String helperName) {
        conditions.add(new Condition(plot.getHelper().contains(helperUuid), "command.error.noHelper", helperName));
        return this;
    }

    public ConditionalExecutor addPlayerCondition(UUID playerUuid) {
        //Error message is already sent by findPlayer method
        conditions.add(new Condition(playerUuid != null, null));
        return this;
    }

    public ConditionalExecutor addModelCondition(String modelName) {
        conditions.add(new Condition(PlotModelManager.existsModel(modelName), "command.model.errorNoModel"));
        return this;
    }

    public ConditionalExecutor addThemeCondition(ThemedBuild theme, String themeName) {
        conditions.add(new Condition(theme!=null,"command.error.noTheme", themeName));
        return this;
    }

    public ConditionalExecutor addVotingCondition(boolean voteAllowed) {
        conditions.add(new Condition(voteAllowed, "command.error.noVoting"));
        return this;
    }

    public void execute(IExecutor executor, String successMessage, String... messageArguments) {
        for(Condition condition: conditions) {
            if(!condition.fulfilled) {
                if(successMessage!=null) {
                    sendError(condition.errorMessage, condition.messageArguments);
                }
                return;
            }
        }
        executor.execute();
        if(successMessage!=null) {
            sendSuccess(successMessage, messageArguments);
        }
    }

    private void sendSuccess(String messageNode, String... replacements) {
        ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get(messageNode,replacements)).send(player);
    }

    private void sendError(String messageNode, String... replacements) {
        ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get(messageNode,replacements)).send(player);
    }

    private record Condition(boolean fulfilled, String errorMessage, String... messageArguments) {}

}
