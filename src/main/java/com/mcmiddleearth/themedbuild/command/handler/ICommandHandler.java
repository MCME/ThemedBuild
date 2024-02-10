package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public interface ICommandHandler {

    default Player getPlayer(CommandContext<McmeCommandSender> context) {
        return (context.getSource() instanceof BukkitPlayer ? ((BukkitPlayer)context.getSource()).getPlayer() : null);
    }

    default boolean hasAnyPermission(McmeCommandSender sender, Permissions... permissions) {
        for(Permissions perm: permissions) {
            if(sender.hasPermission(perm.getNode())) return true;
        }
        return false;
    }

    default boolean hasAllPermissions(McmeCommandSender sender, Permissions... permissions) {
        for(Permissions perm: permissions) {
            if(!sender.hasPermission(perm.getNode())) return false;
        }
        return true;
    }

    default String getBaseCommand(CommandContext<McmeCommandSender> context) {
        return "/"+context.getRootNode().getName();
    }

    default void sendSuccess(CommandContext<McmeCommandSender> context, String messageNode, String... replacements) {
        ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get(messageNode,replacements)).send(getPlayer(context));
    }

    default void sendError(CommandContext<McmeCommandSender> context, String messageNode, String... replacements) {
        ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get(messageNode,replacements)).send(getPlayer(context));
    }



}
