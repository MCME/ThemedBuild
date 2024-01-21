package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public interface ISubcommandExecutor {

    void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder);

    default Player getPlayer(CommandContext<McmeCommandSender> context) {
        return (context.getSource() instanceof BukkitPlayer ? ((BukkitPlayer)context.getSource()).getPlayer() : null);
    }

}
