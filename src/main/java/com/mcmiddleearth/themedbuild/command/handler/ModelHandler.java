package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.argument.PageArgumentType;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.pluginutil.WEUtil;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import com.mcmiddleearth.themedbuild.command.argument.ExistingModelNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.command.handler.executor.ConditionalExecutor;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.PlotModel;
import com.mcmiddleearth.themedbuild.data.PlotModelManager;
import com.mojang.brigadier.context.CommandContext;
import com.sk89q.worldedit.regions.Region;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ModelHandler implements ISubcommandHandler {

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
                        .executes(context -> executeListModelsCommand(context, 1))
                        .then(HelpfulRequiredArgumentBuilder.argument("page",
                                        new PageArgumentType(context -> PlotModelManager.getModels().stream()
                                                .map(PlotModel::getName).collect(Collectors.toList()), MessageUtil.PAGE_LENGTH))
                                .executes(context -> executeListModelsCommand(context, context.getArgument("page", Integer.class)))
                        )
                )
                .then(HelpfulLiteralBuilder.literal("details")
                        .withHelpText(Messages.get("command.model.details.help"))
                        .executes(context -> executeModelDetailsCommand(context, ""))
                        .then(HelpfulRequiredArgumentBuilder.argument("model", new ExistingModelNameArgument())
                                .executes(context -> executeModelDetailsCommand(context, context.getArgument("model",String.class)))
                        )
                )
        );
    }

    private int executeSaveModelCommand(CommandContext<McmeCommandSender> context, String model, String description, boolean overwrite) {
        Region region = WEUtil.getSelection(getPlayer(context));
        new ConditionalExecutor(getPlayer(context))
                .addCondition(region!=null, "command.model.save.error.noSelection")
                .addCondition(!PlotModelManager.existsModel(model) || overwrite, "command.model.save.error.modelExists")
                .execute(()->PlotModelManager.addModel(getPlayer(context), region, model, description),
                                           "command.model.save.success", model);
        /*if(region != null) {
            if(!PlotModelManager.existsModel(model) || overwrite) {
                PlotModelManager.addModel(getPlayer(context), region, model, description);
                sendSuccess(context, "command.model.save.success", model);
            } else {
                sendError(context, "command.model.save.error.modelExists");
            }
        } else {
            sendError(context,"command.model.save.error.noSelection");
        }*/
        return 0;
    }

    private int executeDeleteModelCommand(CommandContext<McmeCommandSender> context, String model) {
        new ConditionalExecutor(getPlayer(context))
            .addModelCondition(model)
            .execute(()->PlotModelManager.deleteModel(model),"command.model.delete.success", model);
        /*if(PlotModelManager.existsModel(model)) {
            PlotModelManager.deleteModel(model);
            sendSuccess(context, Messages.get("command.model.delete.success", model));
        } else {
            sendError(context, "command.model.errorNoModel", model);
        }*/
        return 0;
    }

    private int executeWarpModelCommand(CommandContext<McmeCommandSender> context, String model) {
        new ConditionalExecutor(getPlayer(context))
                .addModelCondition(model)
                .execute(()->getPlayer(context).teleport(Objects.requireNonNull(PlotModelManager.getPlot(model)).getWarpLocation()),
                        "command.model.warp.success", model);
        /*if(PlotModelManager.existsModel(model)) {
            getPlayer(context).teleport(Objects.requireNonNull(PlotModelManager.getPlot(model)).getWarpLocation());
            sendSuccess(context, "command.model.warp.success", model);
        } else {
            sendError(context, "command.model.errorNoModel", model);
        }*/
        return 0;
    }

    private int executeTestModelCommand(CommandContext<McmeCommandSender> context, String model) {
        new ConditionalExecutor(getPlayer(context))
                .addModelCondition(model)
                .execute(()->PlotModelManager.placeModel(model, getPlayer(context)),
                        "command.model.test.success", model);
        /*if(PlotModelManager.existsModel(model)) {
            PlotModelManager.placeModel(model, getPlayer(context));
            sendSuccess(context, "command.model.test.success", model);
        } else {
            sendError(context, "command.model.errorNoModel",model);
        }*/
        return 0;
    }

    private int executeResetModelPlotCommand(CommandContext<McmeCommandSender> context) {
        Plot modelPlot = PlotModelManager.getPlot(getPlayer(context).getLocation());
        new ConditionalExecutor(getPlayer(context))
                .addCondition(modelPlot!=null, "command.model.reset.error")
                .execute(()-> Objects.requireNonNull(modelPlot).reset(),
                        "command.model.test.success");
        /*if(modelPlot!=null) {
            modelPlot.reset();
            sendSuccess(context, "command.model.reset.success");
        } else {
            sendError(context, "command.model.reset.error");
        }*/
        return 0;
    }

    private int executeListModelsCommand(CommandContext<McmeCommandSender> context, int page) {
        FancyMessage header = ThemedBuildPlugin.messageUtil.fancyMessage()
                                                           .addSimple(Messages.get("command.model.list.header"));
        List<FancyMessage> models = PlotModelManager.getModels().stream()
                .map(model -> ThemedBuildPlugin.messageUtil.fancyMessage().addClickable(model.getName(),
                                    context.getRootNode().getName()+"model details "+model.getName())).collect(Collectors.toList());
        ThemedBuildPlugin.messageUtil.sendFancyListMessage(getPlayer(context), header, models,
                                    context.getRootNode().getName()+" model list", page);
        return 0;
    }

    private int executeModelDetailsCommand(CommandContext<McmeCommandSender> context, String modelName) {
        PlotModel model = PlotModelManager.getModel(modelName);
        if(model!=null || PlotModelManager.inPlot(getPlayer(context).getLocation())) {
            if(model==null) {
                model = Objects.requireNonNull(PlotModelManager.getPlot(getPlayer(context).getLocation())).getModel();
            }
            assert model != null;
            sendSuccess(context, "command.model.details.success", modelName,model.getCreator(),
                    ""+model.getSizeX(), ""+model.getSizeZ(), (model.isLimitedHeight()?""+model.getHeight():"unlimited"),
                    model.getDescription());
        } else {
            sendError(context, "command.model.errorNoModel",modelName);
        }
        return 0;
    }
}
