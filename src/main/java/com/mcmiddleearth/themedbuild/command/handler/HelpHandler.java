package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.argument.PageArgumentType;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.node.HelpfulNode;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import com.mcmiddleearth.themedbuild.command.argument.SubcommandArgument;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;

import java.util.List;
import java.util.stream.Collectors;

public class HelpHandler implements ISubcommandHandler {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("help")
                .withHelpText(Messages.get("command.help.help"))
                .executes(context -> executeHelpCommand(context,1))
                .then(HelpfulRequiredArgumentBuilder.argument("page",new PageArgumentType(HelpHandler::getSubcommands, MessageUtil.PAGE_LENGTH))
                        .executes(context -> executeHelpCommand(context, context.getArgument("page", Integer.class)))
                )
                .then(HelpfulRequiredArgumentBuilder.argument("subcommand", new SubcommandArgument())
                        .executes(context -> executeDetailHelpCommand(context, context.getArgument("subcommand",String.class)))
                )
        );
    }

    private int executeHelpCommand(CommandContext<McmeCommandSender> context, int page) {
        List<FancyMessage> subcommands = getSubcommands(context).stream()
                .map(command -> new FancyMessage(ThemedBuildPlugin.messageUtil)
                        .addClickable(getBaseCommand(context)+command+": "+
                                ((HelpfulNode)context.getRootNode().getChild(command)).getHelpText(),
                                getBaseCommand(context)+" help "+command)).collect(Collectors.toList());
        ThemedBuildPlugin.messageUtil.sendFancyListMessage(getPlayer(context),
                                ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get("command.help.header")),
                                            subcommands, getBaseCommand(context)+" help",page);
        return 0;
    }

    private int executeDetailHelpCommand(CommandContext<McmeCommandSender> context,  String subcommand) {
        ThemedBuildPlugin.messageUtil.fancyMessage().addClickable(getBaseCommand(context)+subcommand+": "+
                        ((HelpfulNode)context.getRootNode().getChild(subcommand)).getHelpText(),
                getBaseCommand(context)+subcommand).send(getPlayer(context));
        return 0;
    }

    public static <S> List<String> getSubcommands(CommandContext<S> context) {
        return context.getRootNode().getChildren().stream().map(CommandNode::getName).collect(Collectors.toList());
    }
}
