package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.command.argument.AddablePlayerArgument;
import com.mcmiddleearth.themedbuild.command.argument.RemoveablePlayerArgument;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlotExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("resetplot")
                .withHelpText(Messages.get("command.resetPlot.help"))
                .requires(sender -> sender instanceof BukkitPlayer
                                 && hasAnyPermission(sender, Permissions.MODERATOR, Permissions.BUILDER))
                .executes(this::executeResetPlotCommand)
        )
        .then(HelpfulLiteralBuilder.literal("claim")
                .withHelpText(Messages.get("command.claim.help"))
                .requires(sender -> sender instanceof BukkitPlayer && hasAnyPermission(sender, Permissions.BUILDER))
                .executes(this::executeClaimCommand)
        )
        .then(HelpfulLiteralBuilder.literal("unclaim")
                .withHelpText(Messages.get("command.unclaim.help"))
                .requires(sender -> sender instanceof BukkitPlayer
                        && hasAnyPermission(sender, Permissions.MODERATOR, Permissions.BUILDER))
                .executes(this::executeUnclaimCommand)
        )
        .then(HelpfulLiteralBuilder.literal("add")
                .withHelpText(Messages.get("command.add.help"))
                .requires(sender -> sender instanceof BukkitPlayer && hasAnyPermission(sender, Permissions.BUILDER))
                .then(HelpfulRequiredArgumentBuilder.argument("player", new AddablePlayerArgument())
                        .executes(context -> executeAddCommand(context, context.getArgument("player", String.class)))
                )
        )
        .then(HelpfulLiteralBuilder.literal("remove")
                .withHelpText(Messages.get("command.remove.help"))
                .requires(sender -> sender instanceof BukkitPlayer && hasAnyPermission(sender, Permissions.BUILDER))
                .then(HelpfulRequiredArgumentBuilder.argument("player", new RemoveablePlayerArgument())
                        .executes(context -> executeRemoveCommand(context, context.getArgument("player", String.class)))
                )
        )
        .then(HelpfulLiteralBuilder.literal("commit")
                .withHelpText(Messages.get("command.commit.help"))
                .requires(sender -> sender instanceof BukkitPlayer && hasAnyPermission(sender, Permissions.BUILDER))
                .then(HelpfulRequiredArgumentBuilder.argument("player", new RemoveablePlayerArgument())
                        .executes(context -> executeCommitCommand(context, context.getArgument("player", String.class)))
                )
        )
        .then(HelpfulLiteralBuilder.literal("leave")
                .withHelpText(Messages.get("command.leave.help"))
                .requires(sender -> sender instanceof BukkitPlayer && hasAnyPermission(sender, Permissions.BUILDER))
                .executes(this::executeLeaveCommand)
        );
    }

    private int executeResetPlotCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            plot.reset();
            sendSuccess(context, "command.resetPlot.success");
        } else {
            sendError(context, "command.error.noPlot");
        }
        return 0;
    }

    private int executeClaimCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            if(!plot.isClaimed()) {
                execClaimPlot(context, plot);
            } else {
                sendError(context, "command.claim.errorClaimed");
            }
        } else {
            plot = ThemedBuildManager.getCurrentThemedBuild().getUnclaimedPlot();
            execClaimPlot(context, plot);
        }
        return 0;
    }

    private void execClaimPlot(CommandContext<McmeCommandSender> context, Plot plot) {
        Player player = getPlayer(context);
        if(plot.getThemedbuild().getOwnedPlots(player)<ThemedBuildManager.getMaxOwnedPlotsPerTheme(pl)) {
            plot.claim(player.getUniqueId());
            sendSuccess(context, "command.claim.success");
        } else {
            sendError(context, "command.claim.error.tooMany");
        }
    }

    private int executeUnclaimCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            if(plot.isClaimed()) {
                if(plot.getOwner().equals(getPlayer(context).getUniqueId())) {
                    plot.unclaim();
                    sendSuccess(context, "command.unclaim.success");
                } else {
                    sendError(context, "command.error.notOwned");
                }
            } else {
                sendError(context, "command.error.unclaimed");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }
        return 0;
    }

    private int executeAddCommand(CommandContext<McmeCommandSender> context, String helperName) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            if(!plot.isClaimed()) {
                if(plot.getOwner().equals(getPlayer(context).getUniqueId())) {
                    UUID helper = findPlayerUuid(context, helperName);
                    if(helper == null) {
                        //Error message is already sent by method findPlayerUuid!
                        return 0;
                    }
                    if(!plot.getHelper().contains(helper)) {
                        if(plot.getThemedbuild().getBuildPlots(helper)<ThemedBuildManager.getMaxBuildPlotsPerTheme(helper)) {
                            plot.addHelper(helper);
                            sendSuccess(context, "command.add.success", helperName);
                        } else {
                            sendError(context, "command.add.error.toMany", helperName);
                        }
                    } else {
                        sendError(context, "command.add.error.alreadyHelper", helperName);
                    }
                } else {
                    sendError(context, "command.errorNotOwned");
                }
            } else {
                sendError(context, "command.errorUnclaimed");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }
        return 0;
    }

    private int executeRemoveCommand(CommandContext<McmeCommandSender> context, String helperName) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            if(!plot.isClaimed()) {
                if(plot.getOwner().equals(getPlayer(context).getUniqueId())) {
                    UUID helper = findPlayerUuid(context, helperName);
                    if(helper == null) {
                        //Error message is already sent by method findPlayerUuid!
                        return 0;
                    }
                    if(plot.getHelper().contains(helper)) {
                        plot.removeHelper(helper);
                        sendSuccess(context, "command.remove.success", helperName);
                    } else {
                        sendError(context, "command.remove.errorNoHelper", helperName);
                    }
                } else {
                    sendError(context, "command.error.notOwned");
                }
            } else {
                sendError(context, "command.error.unclaimed");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }
        return 0;
    }

    private int executeCommitCommand(CommandContext<McmeCommandSender> context, String name) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            if(!plot.isClaimed()) {
                if(plot.getOwner().equals(getPlayer(context).getUniqueId())) {
                    UUID newOwner = findPlayerUuid(context, name);
                    if(newOwner == null) {
                        //Error message is already sent by method findPlayerUuid!
                        return 0;
                    }
                    plot.commit(newOwner);
                    sendSuccess(context, "command.commit.success", name);
                } else {
                    sendError(context, "command.error.notOwned");
                }
            } else {
                sendError(context, "command.error.unclaimed");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }
        return 0;
    }

    private int executeLeaveCommand(CommandContext<McmeCommandSender> context) {
        Plot plot = ThemedBuildManager.getPlot(getPlayer(context).getLocation());
        if(plot!=null) {
            if(!plot.isClaimed()) {
                if(plot.getHelper().contains(getPlayer(context).getUniqueId())) {
                    plot.leave(getPlayer(context).getUniqueId());
                    sendSuccess(context, "command.leave.success");
                } else {
                    sendError(context, "command.leave.errorNoHelper");
                }
            } else {
                sendError(context, "command.error.unclaimed");
            }
        } else {
            sendError(context, "command.error.noPlot");
        }
        return 0;
    }

    private UUID findPlayerUuid(CommandContext<McmeCommandSender> context, String playerName) {
        List<? extends OfflinePlayer> matches = Bukkit.matchPlayer(playerName);
        if(matches.isEmpty()) {
            matches = Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(player -> playerName.equalsIgnoreCase(player.getName())).collect(Collectors.toList());
        }
        if(matches.isEmpty()) {
            sendError(context, "command.error.noPlayer", playerName);
            return null;
        } else if(matches.size()>1) {
            sendError(context, "command.error.ambiguousPlayer", playerName);
            return null;
        }
        return matches.get(0).getUniqueId();
    }
}
