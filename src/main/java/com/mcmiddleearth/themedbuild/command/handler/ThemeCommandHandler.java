package com.mcmiddleearth.themedbuild.command.handler;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.handler.BukkitCommandHandler;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import com.mcmiddleearth.themedbuild.command.argument.AllowDenyArgument;
import com.mcmiddleearth.themedbuild.command.argument.ExistingModelNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.PositiveNumberArgument;
import com.mcmiddleearth.themedbuild.command.handler.executor.ConditionalExecutor;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.ThemedBuild;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ThemeCommandHandler extends BukkitCommandHandler implements ICommandHandler {

    public ThemeCommandHandler(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
                .withHelpText(Messages.get("command.theme.help"))
                .requires(sender -> sender.hasPermission(Permissions.VIEWER.getNode()))
                .executes(this::executeBaseCommand)
                .then(HelpfulLiteralBuilder.literal("reload")
                        .withHelpText(Messages.get("command.reload.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .executes(this::executeReloadCommand)
                )
                .then(HelpfulLiteralBuilder.literal("new")
                        .withHelpText(Messages.get("command.new.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", greedyString())
                                        .executes(context -> executeNewCommand(context, context.getArgument("theme",String.class),
                                                context.getArgument("model",String.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("delete")
                        .withHelpText(Messages.get("command.delete.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                .executes(context -> executeDeleteCommand(context, context.getArgument("theme",String.class))))
                )
                .then(HelpfulLiteralBuilder.literal("building")
                        .withHelpText(Messages.get("command.building.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("action", new AllowDenyArgument())
                                .executes(context -> executeSetBuildingCommand(context, ThemedBuildManager.getCurrentThemedBuild().getName(),
                                                                 context.getArgument("action", Boolean.class)))
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                        .executes(context -> executeSetBuildingCommand(context,
                                                                    context.getArgument("theme",String.class),
                                                                    context.getArgument("action", Boolean.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("claiming")
                        .withHelpText(Messages.get("command.claiming.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("action", new AllowDenyArgument())
                                .executes(context -> executeSetClaimingCommand(context,
                                        Objects.requireNonNull(ThemedBuildManager.getCurrentThemedBuild()).getName(),
                                        context.getArgument("action", Boolean.class))
                                )
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                        .executes(context -> executeSetClaimingCommand(context,
                                                context.getArgument("theme",String.class),
                                                context.getArgument("action", Boolean.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("helper")
                        .withHelpText(Messages.get("command.helping.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("action", new AllowDenyArgument())
                                .executes(context -> executeSetHelpingCommand(context,
                                        Objects.requireNonNull(ThemedBuildManager.getCurrentThemedBuild()).getName(),
                                        context.getArgument("action", Boolean.class))
                                )
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                        .executes(context -> executeSetHelpingCommand(context,
                                                context.getArgument("theme",String.class),
                                                context.getArgument("action", Boolean.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("voting")
                        .withHelpText(Messages.get("command.voting.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("action", new AllowDenyArgument())
                                .executes(context -> executeSetVotingCommand(context,
                                        Objects.requireNonNull(ThemedBuildManager.getCurrentThemedBuild()).getName(),
                                        context.getArgument("action", Boolean.class))
                                )
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                        .executes(context -> executeSetVotingCommand(context,
                                                context.getArgument("theme",String.class),
                                                context.getArgument("action", Boolean.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("winner")
                        .withHelpText(Messages.get("command.winner.help"))
                        .requires(sender -> sender instanceof BukkitPlayer && hasAnyPermission(sender,Permissions.RATER))
                        .then(HelpfulRequiredArgumentBuilder.argument("rank", new PositiveNumberArgument())
                                .executes(context -> executeWinnerCommand(context, context.getArgument("rank",Integer.class)))
                        )
                        .then(HelpfulLiteralBuilder.literal("remove")
                                .executes(this::executeWinnerRemoveCommand)
                        )
                )
                .then(HelpfulLiteralBuilder.literal("status")
                        .withHelpText(Messages.get("command.status.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .executes(context -> executeStatusCommand(context, ThemedBuildManager.getCurrentThemedBuild().getName()))
                        .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                .executes(context -> executeStatusCommand(context, context.getArgument("theme",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("setURL")
                        .withHelpText(Messages.get("command.setURL.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("url", word())
                                .executes(context -> executeSetUrlCommand(context, ThemedBuildManager.getCurrentThemedBuild().getName(),
                                                                          context.getArgument("url",String.class)))
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                        .executes(context -> executeSetUrlCommand(context, context.getArgument("theme",String.class),
                                                                                           context.getArgument("url",String.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("setRP")
                        .withHelpText(Messages.get("command.setRP.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("rpList", greedyString())
                                .executes(context -> executeSetRP(context, context.getArgument("rpList",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("info")
                        .withHelpText(Messages.get("command.info.help"))
                        .requires(sender -> hasAnyPermission(sender, Permissions.VIEWER))
                        .executes(context -> executeInfoCommand(context, ThemedBuildManager.getCurrentThemedBuild().getName()))
                        .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                .executes(context -> executeInfoCommand(context, context.getArgument("theme",String.class)))
                        )
                );
        new HelpHandler().addCommandTree(helpfulLiteralBuilder);
        new ToplotHandler().addCommandTree(helpfulLiteralBuilder);
        new WarpHandler().addCommandTree(helpfulLiteralBuilder);
        new PlotHandler().addCommandTree(helpfulLiteralBuilder);
        new ModelHandler().addCommandTree(helpfulLiteralBuilder);
        new VoteHandler().addCommandTree(helpfulLiteralBuilder);
        return helpfulLiteralBuilder;
    }

    private int executeSetBuildingCommand(CommandContext<McmeCommandSender> context, String themeName, boolean allow) {
        ThemedBuild themedBuild = ThemedBuildManager.getThemedBuild(themeName);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addThemeCondition(themedBuild, themeName)
                .addCondition(Objects.requireNonNull(themedBuild).isBuildingAllowed()!=allow,
                              (allow?"command.building.ErrorAlreadyAllowed":"command.building.ErrorAlreadyDenied"),themeName)
                .execute(()->themedBuild.setAllowBuilding(allow),
                          (allow?"command.building.success.allow":"command.building.success.deny"),themeName);
        return 0;
    }

    private int executeSetClaimingCommand(CommandContext<McmeCommandSender> context, String themeName, boolean allow) {
        ThemedBuild themedBuild = ThemedBuildManager.getThemedBuild(themeName);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addThemeCondition(themedBuild, themeName)
                .addCondition(Objects.requireNonNull(themedBuild).isClaimingAllowed()!=allow,
                        (allow?"command.claiming.errorAlreadyAllowed":"command.claiming.errorAlreadyDenied"),themeName)
                .execute(()->themedBuild.setAllowClaiming(allow),
                        (allow?"command.claiming.success.allow":"command.claiming.success.deny"),themeName);
        return 0;
    }

    private int executeSetHelpingCommand(CommandContext<McmeCommandSender> context, String themeName, boolean allow) {
        ThemedBuild themedBuild = ThemedBuildManager.getThemedBuild(themeName);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addThemeCondition(themedBuild, themeName)
                .addCondition(Objects.requireNonNull(themedBuild).isHelpingAllowed()!=allow,
                        (allow?"command.helping.errorAlreadyAllowed":"command.helping.errorAlreadyDenied"),themeName)
                .execute(()->themedBuild.setAllowHelping(allow),
                        (allow?"command.helping.success.allow":"command.helping.success.deny"),themeName);
        return 0;
    }

    private int executeSetVotingCommand(CommandContext<McmeCommandSender> context, String themeName, Boolean allow) {
        ThemedBuild themedBuild = ThemedBuildManager.getThemedBuild(themeName);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addThemeCondition(themedBuild, themeName)
                .addCondition(Objects.requireNonNull(themedBuild).isVotingAllowed()!=allow,
                        (allow?"command.voting.errorAlreadyAllowed":"command.voting.errorAlreadyDenied"),themeName)
                .execute(()->themedBuild.setAllowVoting(allow),
                        (allow?"command.voting.success.allow":"command.voting.success.deny"),themeName);
        return 0;
    }

    private int executeWinnerCommand(CommandContext<McmeCommandSender> context, int rank) {
        Player player = getPlayer(context);
        Plot plot = ThemedBuildManager.getPlot(player.getLocation());
        new ConditionalExecutor(player)
                .addPlotCondition(plot)
                .addCondition(Objects.requireNonNull(plot).getRank()!=rank,
                              "command.winner.errorSameRank", ""+rank)
                .execute(()-> plot.getThemedBuild().setWinner(plot, rank),"command.winner.success");
        return 0;
    }

    private int executeWinnerRemoveCommand(CommandContext<McmeCommandSender> context) {
        Player player = getPlayer(context);
        Plot plot = ThemedBuildManager.getPlot(player.getLocation());
        new ConditionalExecutor(player)
                .addPlotCondition(plot)
                .addCondition(Objects.requireNonNull(plot).isWinner(),
                              "command.winner.remove.errorNoWinner")
                .execute(()-> plot.getThemedBuild().removeWinner(plot),"command.winner.remove.success");
        return 0;
    }

    private int executeBaseCommand(CommandContext<McmeCommandSender> context) {
        //todo: ???
        return 0;
    }

    private int executeReloadCommand(CommandContext<McmeCommandSender> context) {
        ThemedBuildPlugin.getPluginInstance().reload();
        sendSuccess(context, "command.reload.success");
        return 0;
    }

    private int executeNewCommand(CommandContext<McmeCommandSender> context, String themeName, String modelName) {
        Player player = getPlayer(context);
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        new ConditionalExecutor(player)
                .addCondition(theme==null, "command.new.errorAlreadyExists", themeName)
                .addModelCondition(modelName)
                .execute(()->ThemedBuildManager.createNewTheme(themeName, modelName), "command.new.success",themeName, modelName);
        return 0;
    }

    private int executeDeleteCommand(CommandContext<McmeCommandSender> context, String themeName) {
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addCondition(theme==null, "command.delete.error.noTheme", themeName)
                .addCondition(theme!=ThemedBuildManager.getCurrentThemedBuild(), "command.delete.error.notCurrent", themeName)
                .execute(ThemedBuildManager::deleteCurrentTheme, "command.delete,success");
        return 0;
    }

    private int executeStatusCommand(CommandContext<McmeCommandSender> context, String themeName) {
        Player player = getPlayer(context);
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        new ConditionalExecutor(player)
                .addThemeCondition(theme, themeName)
                .execute(()-> {
                    sendSuccess(context, "command.status.success", themeName);
                    sendSuccess(context, "Claimed plots: "+ Objects.requireNonNull(theme).getClaimedPlots().size());
                    sendSuccess(context, "Resource packs: "+ Joiner.on(", ").join(theme.getResourcePacks()));
                    sendSuccess(context, "Building: "+(theme.isBuildingAllowed()?"allowed":"denied"));
                    sendSuccess(context, "Claiming: "+(theme.isClaimingAllowed()?"allowed":"denied"));
                    sendSuccess(context, "Helping: "+(theme.isHelpingAllowed()?"allowed":"denied"));
                    sendSuccess(context, "Voting: "+(theme.isVotingAllowed()?"allowed":"denied"));
                }, null);
        return 0;
    }

    private int executeSetUrlCommand(CommandContext<McmeCommandSender> context, String themeName, String url) {
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        new ConditionalExecutor(getPlayer(context))
                .addThemeCondition(theme, themeName)
                .execute(()-> Objects.requireNonNull(theme).setURL(url), "command.setURL.success");
        return 0;
    }

    private int executeSetRP(CommandContext<McmeCommandSender> context, String rpList) {
        String[] resourcePacks = rpList.split(" ");
        new ConditionalExecutor(getPlayer(context))
                .execute(()-> ThemedBuildManager.getCurrentThemedBuild().setRP(resourcePacks),"command.setRP.success", rpList);

        return 0;
    }

    private int executeInfoCommand(CommandContext<McmeCommandSender> context, String themeName) {
        Player player = getPlayer(context);
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        new ConditionalExecutor(player)
                .addThemeCondition(theme, themeName)
                .execute(()-> {
                    sendSuccess(context, "command.info.success", themeName);
                    sendSuccess(context, "Instructions at: "+ Objects.requireNonNull(theme).getURL());
                },null);
        return 0;
    }

}
