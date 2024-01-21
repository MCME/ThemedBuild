package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.handler.BukkitCommandHandler;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.command.argument.*;
import com.mojang.brigadier.context.CommandContext;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ThemeCommandExecutor extends BukkitCommandHandler {

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
                .requires(sender -> sender.hasPermission(Permissions.VIEWER.getNode()))
                .executes(this::executeBaseCommand)
                .then(HelpfulLiteralBuilder.literal("reload")
                        .requires(sender -> sender.hasPermission(Permissions.MANAGER.getNode()))
                        .executes(this::executeReloadCommand)
                )
                .then(HelpfulLiteralBuilder.literal("new")
                        .requires(sender -> sender.hasPermission(Permissions.MANAGER.getNode()))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .then(HelpfulRequiredArgumentBuilder.argument("theme", new AvailableThemedbuildNameArgument())
                                        .executes(context -> executeNewCommand(context, context.getArgument("theme",String.class),
                                                context.getArgument("model",String.class)))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("setURL")
                        .requires(sender -> sender.hasPermission(Permissions.MANAGER.getNode()))
                        .then(HelpfulRequiredArgumentBuilder.argument("url", word())
                                .executes(context -> executeSetUrlCommand(context, context.getArgument("url",String.class)))
                        )
                );
        new HelpExecutor().addCommandTree(helpfulLiteralBuilder);
        new ToplotExecutor().addCommandTree(helpfulLiteralBuilder);
        new WarpExecutor().addCommandTree(helpfulLiteralBuilder);
        new PlotExecutor().addCommandTree(helpfulLiteralBuilder);
        new ModelExecutor().addCommandTree(helpfulLiteralBuilder);
        return helpfulLiteralBuilder;
    }

    private int executeBaseCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeReloadCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeNewCommand(CommandContext<McmeCommandSender> context, String theme, String model) {
        return 0;
    }

    private int executeSetUrlCommand(CommandContext<McmeCommandSender> context, String url) {
        return 0;
    }

}
