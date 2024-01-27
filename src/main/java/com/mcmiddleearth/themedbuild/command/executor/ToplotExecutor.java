package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.command.argument.OwnedPlotNumberArgument;
import com.mcmiddleearth.themedbuild.data.ThemedbuildManager;
import com.mcmiddleearth.themedbuild.domain.Plot;
import com.mojang.brigadier.context.CommandContext;

public class ToplotExecutor implements ISubcommandExecutor {

    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("toplot")
                .withHelpText(Messages.get("command.toplot.help"))
                .requires(sender -> sender instanceof BukkitPlayer)
                .executes(context -> executeToplotCommand(context, ThemedbuildManager.getCurrentThemedbuild()
                        .getPlot(getPlayer(context).getUniqueId(),0)))
                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                        .executes(context -> executeToplotCommand(context, ThemedbuildManager
                                .getThemedbuild(context.getArgument("theme", String.class))
                                .getPlot(getPlayer(context).getUniqueId(),0)))
                        .then(HelpfulRequiredArgumentBuilder.argument("number", new OwnedPlotNumberArgument())
                                .executes(context -> executeToplotCommand(context, ThemedbuildManager
                                        .getThemedbuild(context.getArgument("theme", String.class))
                                        .getPlot(getPlayer(context).getUniqueId(),
                                                context.getArgument("number",Integer.class))))
                        )
                )
        );
    }

    private static int executeToplotCommand(CommandContext<McmeCommandSender> context, Plot plot) {

        return 0;
    }

}
