package com.mcmiddleearth.themedbuild.command.executor;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.Permissions;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Player;

public interface ISubcommandExecutor extends ICommandExecutor {

    void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder);

}
