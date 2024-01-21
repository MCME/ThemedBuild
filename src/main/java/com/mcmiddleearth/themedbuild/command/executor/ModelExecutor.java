package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mojang.brigadier.context.CommandContext;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ModelExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("model")
                .requires(sender -> sender.hasPermission(Permissions.MANAGER.getNode()))
                .then(HelpfulLiteralBuilder.literal("save")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", word())
                                .executes(context -> executeSaveModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("delete")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", word())
                                .executes(context -> executeDeleteModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("test")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", word())
                                .executes(context -> executeTestModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("list")
                        .executes(this::executeListModelsCommand)
                )
        );
    }

    private int executeSaveModelCommand(CommandContext<McmeCommandSender> context, String model) {
        return 0;
    }

    private int executeDeleteModelCommand(CommandContext<McmeCommandSender> context, String model) {
        return 0;
    }

    private int executeTestModelCommand(CommandContext<McmeCommandSender> context, String model) {
        return 0;
    }

    private int executeListModelsCommand(CommandContext<McmeCommandSender> context) {
        return 0;
    }
}
