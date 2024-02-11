package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import com.mcmiddleearth.themedbuild.command.handler.executor.ConditionalExecutor;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class VoteHandler implements ISubcommandHandler {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("vote")
                .withHelpText(Messages.get("command.vote.help"))
                .requires(sender -> hasAnyPermission(sender, Permissions.VOTER))
                .executes(this::executeAddVoteCommand)
                .then(HelpfulLiteralBuilder.literal("diamond")
                        .requires(sender -> hasAnyPermission(sender, Permissions.STAR_VOTER))
                        .executes(this::executeAddStarCommand)
                )
                .then(HelpfulLiteralBuilder.literal("remove")
                        .executes(this::executeRemoveVoteCommand)
                )
        );
    }

    private int executeAddVoteCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addPlotCondition(plot)
                .addVotingCondition(plot.isVotingAllowed())
                .addCondition(!plot.hasVoted(player.getUniqueId()),"command.addVote.errorAlreadyVoted")
                .execute(()->plot.addVote(player.getUniqueId(), getVoteFactor(context)), "command.addVote.success");
        /*if(plot!=null) {
            if(plot.isVotingAllowed()) {
                if(!plot.hasVoted(getPlayer(context).getUniqueId())) {
                    plot.addVote(getPlayer(context).getUniqueId(), getVoteFactor(context));
                    sendSuccess(context, "command.addVote.success");
                } else {
                    sendError(context, "command.addVote.errorAlreadyVoted");
                }
            } else {
                sendError(context, "command.error.noVoting");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }*/
        return 0;
    }

    private int executeAddStarCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addPlotCondition(plot)
                .addVotingCondition(plot.isVotingAllowed())
                .addCondition(!plot.hasDiamond(getPlayer(context).getUniqueId()),"command.addDiamond.errorAlreadyVoted")
                .execute(()->plot.addVote(player.getUniqueId(), getVoteFactor(context)), "command.addDiamond.success");
        /*if(plot!=null) {
            if(plot.isVotingAllowed()) {
                if(!plot.hasDiamond(getPlayer(context).getUniqueId())) {
                    plot.addDiamond(getPlayer(context).getUniqueId(), getVoteFactor(context));
                    sendSuccess(context, "command.addDiamond.success");
                } else {
                    sendError(context, "command.addDiamond.errorAlreadyVoted");
                }
            } else {
                sendError(context, "command.error.noVoting");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }*/
        return 0;
    }

    private int executeRemoveVoteCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addPlotCondition(plot)
                .addVotingCondition(plot.isVotingAllowed())
                .addCondition(plot.hasVoted(player.getUniqueId()),"command.removeVote.errorNotVoted")
                .execute(()->plot.removeVote(player.getUniqueId()), "command.removeVote.success");
        /*if(plot!=null) {
            if(plot.isVotingAllowed()) {
                if(plot.hasVoted(getPlayer(context).getUniqueId())) {
                    plot.removeVote(getPlayer(context).getUniqueId());
                    sendSuccess(context, "command.removeVote.success");
                } else {
                    sendError(context, "command.removeVote.errorNotVoted");
                }
            } else {
                sendError(context, "command.error.noVoting");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }*/
        return 0;
    }

    private int getVoteFactor(CommandContext<McmeCommandSender> context) {
        if(hasAnyPermission(context.getSource(),Permissions.POWER_VOTER)) {
            return ThemedBuildPlugin.getConfig().getInt("powerVoteFactor",2);
        } else {
            return 1;
        }
    }
}
