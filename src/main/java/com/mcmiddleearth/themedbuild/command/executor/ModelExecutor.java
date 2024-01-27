package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.pluginutil.WEUtil;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
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
                .withHelpText(Messages.get("command.model.help"))
                .requires(sender -> hasAnyPermission(sender,Permissions.MANAGER))
                .then(HelpfulLiteralBuilder.literal("save")
                        .withHelpText(Messages.get("command.model.save.help"))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", word())
                                .then(HelpfulRequiredArgumentBuilder.argument("description", greedyString())
                                        .executes(context -> executeSaveModelCommand(context,
                                                                    context.getArgument("model", String.class),
                                                                    context.getArgument("description", String.class),false))
                                )
                                .then(HelpfulLiteralBuilder.literal("-o")
                                        .then(HelpfulRequiredArgumentBuilder.argument("description", greedyString()))
                                        .executes(context -> executeSaveModelCommand(context,
                                                context.getArgument("model", String.class),
                                                context.getArgument("description", String.class),true))
                                )
                        )
                )
                .then(HelpfulLiteralBuilder.literal("delete")
                        .withHelpText(Messages.get("command.model.delete.help"))
                        .executes(context -> executeDeleteModelCommand(context, null)) //delete plot sender ist standing in
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .executes(context -> executeDeleteModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("warp")
                        .withHelpText(Messages.get("command.model.warp.help"))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .executes(context -> executeWarpModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("test")
                        .withHelpText(Messages.get("command.model.test.help"))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingThemeNameArgument())
                                .executes(context -> executeTestModelCommand(context, context.getArgument("model",String.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("resetplot")
                        .withHelpText(Messages.get("command.model.resetplot.help"))
                        .executes(this::executeResetModelPlotCommand)
                )
                .then(HelpfulLiteralBuilder.literal("list")
                        .withHelpText(Messages.get("command.model.list.help"))
                        .executes(this::executeListModelsCommand)
                )
                .then(HelpfulLiteralBuilder.literal("details")
                        .withHelpText(Messages.get("command.model.details.help"))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .executes(context -> executeModelDetailsCommand(context, context.getArgument("model",String.class)))
                        )
                )
        );
    }

    private int executeSaveModelCommand(CommandContext<McmeCommandSender> context, String model, String description, boolean overwrite) {
        Region region = WEUtil.getSelection(getPlayer(context));
        if(region != null) {
            if(!ModelManager.existsModel(model) || overwrite) {
                ModelManager.addModel(getPlayer(context), region, model, description);
                ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.save.success", model)).send(getPlayer(context));
            } else {
                ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.save.error.modelExists"));
            }
        } else {
            ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.save.error.noSelection")).send(getPlayer(context));
        }
        return 0;
    }

    private int executeDeleteModelCommand(CommandContext<McmeCommandSender> context, String model) {
        if(ModelManager.existsModel(model)) {
            ModelManager.deleteModel(model);
            ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.delete.success", model)).send(getPlayer(context));
        } else {
            ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.delete.error", model)).send(getPlayer(context));
        }
        return 0;
    }

    private int executeWarpModelCommand(CommandContext<McmeCommandSender> context, String model) {
        if(ModelManager.existsModel(model)) {
            getPlayer(context).teleport(ModelManager.getPlot(model).getLocation());
            ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.warp.success", model)).send(getPlayer(context));
        } else {
            ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.warp.error", model)).send(getPlayer(context));
        }
        return 0;
    }

    private int executeTestModelCommand(CommandContext<McmeCommandSender> context, String model) {
        if(ModelManager.existsModel(model)) {
            ModelManager.placeModel(model);
            ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.test.success", model)).send(getPlayer(context));
        } else {
            ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.test.error",model)).send(getPlayer(context));
        }
        return 0;
    }

    private int executeResetModelPlotCommand(CommandContext<McmeCommandSender> context) {
        if(ModelManager.inPlot(getPlayer(context))) {
            ModelManager.resetPlotModel(ModelManager.getPlot(getPlayer(context).getLocation()));
            ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.reset.success")).send(getPlayer(context));
        } else {
            ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.reset.error")).send(getPlayer(context));
        }
        return 0;
    }

    private int executeListModelsCommand(CommandContext<McmeCommandSender> context) {
            ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.list.header")).send(getPlayer(context));
        return 0;
    }

    private int executeModelDetailsCommand(CommandContext<McmeCommandSender> context, String model) {
        if(ModelManager.existsModel(model)) {

            ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.model.details.success.header", model)).send(getPlayer(context));
        } else {
            ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get("command.model.details.error",model)).send(getPlayer(context));
        }
        return 0;
    }
}
