package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.command.argument.ExistingThemeNameArgument;
import com.mcmiddleearth.themedbuild.command.handler.executor.ConditionalExecutor;
import com.mcmiddleearth.themedbuild.data.ThemedBuild;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public class WarpHandler implements ISubcommandHandler {


    @Override
    public void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder) {
        helpfulLiteralBuilder
        .then(HelpfulLiteralBuilder.literal("warp")
                .withHelpText(Messages.get("command.warp.help"))
                .requires(sender -> sender instanceof BukkitPlayer)
                .executes(context -> executeWarpCommand(context, ThemedBuildManager.getCurrentThemedbuild().getName()))
                .then(HelpfulRequiredArgumentBuilder.argument("theme", new ExistingThemeNameArgument())
                        .executes(context -> executeWarpCommand(context, context.getArgument("theme",String.class)))
                )
        );
    }

    private int executeWarpCommand(CommandContext<McmeCommandSender> context, String theme) {
        ThemedBuild themedBuild = ThemedBuildManager.getThemedBuild(theme);
        Player player = getPlayer(context);
        new ConditionalExecutor(player)
                .addThemeCondition(themedBuild, theme)
                .execute(()->player.teleport(themedBuild.getWarpLocation()), "command.warp.success");
        /*if(themedBuild != null) {
            getPlayer(context).teleport(themedBuild.getWarpLocation());
            sendSuccess(context, "command.warp.success");
        } else {
            sendError(context, "command.warp.error",theme);
        }*/
        return 0;
    }

}
