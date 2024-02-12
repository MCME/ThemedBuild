package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.PlotNumberArgument;
import com.mcmiddleearth.themedbuild.command.handler.executor.ConditionalExecutor;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.ThemedBuild;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ToplotHandler implements ISubcommandHandler {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("toplot")
                .withHelpText(Messages.get("command.toplot.help"))
                .requires(sender -> sender instanceof BukkitPlayer)
                .executes(context -> executeToplotCommand(context,
                                                    ThemedBuildManager.getCurrentThemedBuild().getName(),0))
                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                        .executes(context -> executeToplotCommand(context,
                                                    context.getArgument("theme", String.class),0))
                        .then(HelpfulRequiredArgumentBuilder.argument("number", new PlotNumberArgument())
                                .executes(context -> executeToplotCommand(context,
                                                    context.getArgument("theme", String.class),
                                                    context.getArgument("number",Integer.class)))
                        )
                )
        );
    }

    private int executeToplotCommand(CommandContext<McmeCommandSender> context, String themeName, int plotNummer) {
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addThemeCondition(theme, themeName)
                .execute(()->{
                    assert theme != null;
                    Plot plot = theme.getPlot(player.getUniqueId(),plotNummer);
                    new ConditionalExecutor(player)
                            .addCondition(plot!=null, "command.toPlot.errorNoPlot", ""+plotNummer, themeName)
                            .execute(()->player.teleport(Objects.requireNonNull(plot).getWarpLocation()),"command.toPlot.success",
                                            ""+plotNummer, themeName);
                    /*if(plot != null) {
                        getPlayer(context).teleport(plot.getWarpLocation());
                        sendSuccess(context, "command.toPlot.success", ""+plotNummer, themeName, plot.getOwner());
                    } else {
                        sendError(context, "command.toPlot.errorNoPlot", ""+plotNummer, themeName);
                    }*/
                },null);
        return 0;
    }

}
