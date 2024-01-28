package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.argument.BukkitOfflinePlayerArgument;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.handler.BukkitCommandHandler;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.command.argument.*;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.context.CommandContext;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ThemeCommandExecutor extends BukkitCommandHandler implements ICommandExecutor {

    public ThemeCommandExecutor(String command) {
        super(command);
    }
    /*Available subcommands:
            §2/theme toplot§f -- teleports you to your plot in current theme
      §2/theme warp§f -- teleports you to the current theme
      §2/theme resetplot§f -- restores plot to original state
      §2/theme unclaim§f -- unclaims a plot
      §2/theme help [subcommand]§f -- view more informations about subcommand
      §2/theme new <name>§f -- create new Themed Build
      §2/theme set <name>§f -- start new chain of Themed Builds
      §2/theme createmodel <name>§f -- create empty plot model
      §2/theme modelpos <1|2>§f -- set point at feet coordinates
      §2/theme deletemodel <name>§f -- delete plot model
      §2/theme savemodel§f -- save current model
      §2/theme listmodels§f -- list available models
      §2/theme setURL <url>§f -- set the URL for the themedbuild*/

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
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new AvailableThemedbuildNameArgument())
                                        .executes(context -> executeNewCommand(context, context.getArgument("theme",String.class),
                                                context.getArgument("model",String.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("building")
                        .withHelpText(Messages.get("command.building.help"))
                        .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                        .then(HelpfulRequiredArgumentBuilder.argument("action", new AllowDenyArgument())
                                .executes(context -> {
                                    assert ThemedBuildManager.getCurrentThemedBuild() != null;
                                    return executeSetBuildingCommand(context, ThemedBuildManager.getCurrentThemedBuild().getName(),
                                                                     context.getArgument("action", Boolean.class));
                                })
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
                                .executes(context -> executeSetClaimingCommand(context, ThemedBuildManager.getCurrentThemedBuild().getName(),
                                            context.getArgument("action", Boolean.class))
                                )
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                                        .executes(context -> executeSetClaimingCommand(context,
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
                );
        new HelpExecutor().addCommandTree(helpfulLiteralBuilder);
        new ToplotExecutor().addCommandTree(helpfulLiteralBuilder);
        new WarpExecutor().addCommandTree(helpfulLiteralBuilder);
        new PlotExecutor().addCommandTree(helpfulLiteralBuilder);
        new ModelExecutor().addCommandTree(helpfulLiteralBuilder);
        return helpfulLiteralBuilder;
    }

    private int executeSetBuildingCommand(CommandContext<McmeCommandSender> context, String themeName, boolean allow) {
        return 0;
    }

    private int executeSetClaimingCommand(CommandContext<McmeCommandSender> context, String themeName, boolean allow) {
        return 0;
    }

    private int executeWinnerCommand(CommandContext<McmeCommandSender> context, String themeName, int rank) {
        return 0;
    }

    private int executeWinnerRemoveCommand(CommandContext<McmeCommandSender> context, String themeName, int rank) {
        return 0;
    }

    private int executeBaseCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeReloadCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeNewCommand(CommandContext<McmeCommandSender> context, String themeName, String model) {
        return 0;
    }

    private int executeStatusCommand(CommandContext<McmeCommandSender> context, String themeName) {
        return 0;
    }

    private int executeSetUrlCommand(CommandContext<McmeCommandSender> context, String themeName, String url) {
        return 0;
    }

}
