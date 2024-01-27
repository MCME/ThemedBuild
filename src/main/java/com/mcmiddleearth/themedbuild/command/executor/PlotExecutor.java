package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.command.argument.AddablePlayerArgument;
import com.mcmiddleearth.themedbuild.command.argument.RemoveablePlayerArgument;
import com.mojang.brigadier.context.CommandContext;

public class PlotExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("resetplot")
                .withHelpText(Messages.get("command.resetplot.help"))
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
