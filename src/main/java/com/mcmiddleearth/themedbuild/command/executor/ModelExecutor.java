package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.command.argument.ExistingModelNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mojang.brigadier.context.CommandContext;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ModelExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("model")
                .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                .then(HelpfulLiteralBuilder.literal("save")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", word())
                                .then(HelpfulRequiredArgumentBuilder.argument("description", greedyString()))
                                        .executes(context -> executeSaveModelCommand(context,
                                                                    context.getArgument("model", String.class),
                                                                    context.getArgument("description", String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("delete")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .executes(context -> executeDeleteModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("test")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingThemeNameArgument())
                                .executes(context -> executeTestModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("list")
                        .executes(this::executeListModelsCommand)
                )
                .then(HelpfulLiteralBuilder.literal("details")
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .executes(context -> executeModelDetailsCommand(context, context.getArgument("model",String.class)))
                        )
                )
        );
    }

    private int executeSaveModelCommand(CommandContext<McmeCommandSender> context, String model, String description) {
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

    private int executeModelDetailsCommand(CommandContext<McmeCommandSender> context, String model) {
        return 0;
    }
}
