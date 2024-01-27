package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public interface ICommandExecutor {

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

}
