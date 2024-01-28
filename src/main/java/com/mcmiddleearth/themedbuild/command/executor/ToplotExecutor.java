package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.OwnedPlotNumberArgument;
import com.mcmiddleearth.themedbuild.data.ThemedBuild;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mcmiddleearth.themedbuild.domain.Plot;
import com.mojang.brigadier.context.CommandContext;

public class ToplotExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("toplot")
                .withHelpText(Messages.get("command.toplot.help"))
                .requires(sender -> sender instanceof BukkitPlayer)
                .executes(context -> executeToplotCommand(context,
                                                    ThemedBuildManager.getCurrentThemedbuild().getName(),0))
                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                        .executes(context -> executeToplotCommand(context,
                                                    context.getArgument("theme", String.class),0))
                        .then(HelpfulRequiredArgumentBuilder.argument("number", new OwnedPlotNumberArgument())
                                .executes(context -> executeToplotCommand(context,
                                                    context.getArgument("theme", String.class),
                                                    context.getArgument("number",Integer.class)))
                        )
                )
        );
    }

    private int executeToplotCommand(CommandContext<McmeCommandSender> context, String themeName, int plotNummer) {
        ThemedBuild theme = ThemedBuildManager.getThemedBuild(themeName);
        if(theme != null) {
            Plot plot = theme.getPlot(getPlayer(context).getUniqueId(),plotNummer);
            if(plot != null) {
                getPlayer(context).teleport(plot.getWarpLocation());
                sendSuccess(context, "command.toPlot.success", ""+plotNummer, themeName, plot.getOwner());
            } else {
                sendError(context, "command.toPlot.errorNoPlot", ""+plotNummer, themeName);
            }
        } else {
            sendError(context, "command.error.noTheme", themeName);
        }
        return 0;
    }

}
