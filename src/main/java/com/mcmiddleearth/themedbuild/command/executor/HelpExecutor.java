package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.command.argument.SubcommandArgument;
import com.mojang.brigadier.context.CommandContext;

public class HelpExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("help")
                .executes(this::executeHelpCommand)
                .then(HelpfulRequiredArgumentBuilder.argument("subcommand", new SubcommandArgument())
                        .executes(context -> executeDetailHelpCommand(context, context.getArgument("subcommand",String.class)))
                )
        );
    }

    private int executeHelpCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }

    private int executeDetailHelpCommand(CommandContext<McmeCommandSender> context,  String subcommand) {
        return 0;
    }
}
