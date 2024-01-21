package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.command.argument.AddablePlayerArgument;
import com.mcmiddleearth.themedbuild.command.argument.RemoveablePlayerArgument;
import com.mojang.brigadier.context.CommandContext;

public class PlotExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("resetplot")
                .requires(sender -> sender instanceof BukkitPlayer &&
                        (sender.hasPermission(Permissions.MODERATOR.getNode())
                                || sender.hasPermission(Permissions.BUILDER.getNode())))
                .executes(this::executeResetPlotCommand)
        )
        .then(HelpfulLiteralBuilder.literal("claim")
                .requires(sender -> sender.hasPermission(Permissions.BUILDER.getNode()) && sender instanceof BukkitPlayer)
                .executes(this::executeClaimCommand)
        )
        .then(HelpfulLiteralBuilder.literal("unclaim")
                .requires(sender -> sender instanceof BukkitPlayer &&
                        (sender.hasPermission(Permissions.MODERATOR.getNode())
                                || sender.hasPermission(Permissions.BUILDER.getNode())))
                .executes(this::executeUnclaimCommand)
        )
        .then(HelpfulLiteralBuilder.literal("add")
                .requires(sender -> sender.hasPermission(Permissions.BUILDER.getNode()) && sender instanceof BukkitPlayer)
                .then(HelpfulRequiredArgumentBuilder.argument("player", new AddablePlayerArgument())
                        .executes(context -> executeAddCommand(context, context.getArgument("player", String.class)))
                )
        )
        .then(HelpfulLiteralBuilder.literal("remove")
                .requires(sender -> sender.hasPermission(Permissions.BUILDER.getNode()) && sender instanceof BukkitPlayer)
                .then(HelpfulRequiredArgumentBuilder.argument("player", new RemoveablePlayerArgument())
                        .executes(context -> executeRemoveCommand(context, context.getArgument("player", String.class)))
                )
        )
        .then(HelpfulLiteralBuilder.literal("commit")
                .requires(sender -> sender.hasPermission(Permissions.BUILDER.getNode()) && sender instanceof BukkitPlayer)
                .then(HelpfulRequiredArgumentBuilder.argument("player", new RemoveablePlayerArgument())
                        .executes(context -> executeCommitCommand(context, context.getArgument("player", String.class)))
                )
        )
        .then(HelpfulLiteralBuilder.literal("leave")
                .requires(sender -> sender.hasPermission(Permissions.BUILDER.getNode()) && sender instanceof BukkitPlayer)
                .executes(this::executeLeaveCommand)
        );
    }

    private int executeResetPlotCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeClaimCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeUnclaimCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeAddCommand(CommandContext<McmeCommandSender> context, String player) {
        return 0;
    }

    private int executeRemoveCommand(CommandContext<McmeCommandSender> context, String player) {
        return 0;
    }

    private int executeCommitCommand(CommandContext<McmeCommandSender> context, String player) {
        return 0;
    }

    private int executeLeaveCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }
}
