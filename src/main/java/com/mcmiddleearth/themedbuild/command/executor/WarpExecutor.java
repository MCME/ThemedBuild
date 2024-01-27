package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.data.ThemedbuildManager;
import com.mojang.brigadier.context.CommandContext;

public class WarpExecutor implements ISubcommandExecutor {


    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("warp")
                .withHelpText(Messages.get("command.warp.help"))
                .requires(sender -> sender instanceof BukkitPlayer)
                .executes(context -> executeWarpCommand(context, ThemedbuildManager.getCurrentThemedbuild().getName()))
                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                        .executes(context -> executeWarpCommand(context, context.getArgument("theme",String.class)))
                )
        );
    }

    private static int executeWarpCommand(CommandContext<McmeCommandSender> context, String theme) {
        return 0;
    }

}
