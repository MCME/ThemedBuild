package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mojang.brigadier.context.CommandContext;

public class VoteExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("vote")
                .withHelpText(Messages.get("command.vote.help"))
                .requires(sender -> hasAnyPermission(sender, Permissions.VOTER))
                .executes(this::executeAddVoteCommand)
                .then(HelpfulLiteralBuilder.literal("star")
                        .requires(sender -> hasAnyPermission(sender, Permissions.STAR_VOTER))
                        .executes(this::executeAddStarCommand)
                )
                .then(HelpfulLiteralBuilder.literal("remove")
                        .executes(this::executeRemoveVoteCommand)
                )
        );
    }

    private int executeAddVoteCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeAddStarCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeRemoveVoteCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }


}
