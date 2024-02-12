package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.HelpfulArgumentType;
import com.mcmiddleearth.command.sender.BukkitPlayer;
import com.mcmiddleearth.themedbuild.data.Plot;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;

public class PlotPlayerArgument implements HelpfulArgumentType, ArgumentType<String> {

    private String tooltip = "A player with a certain relation to a Themed-build plot.";

    private final BiPredicate<UUID, Plot> predicate;

    public PlotPlayerArgument(BiPredicate<UUID, Plot> predicate) {
        this.predicate = predicate;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if(context.getSource() instanceof BukkitPlayer bukkitPlayer) {
            Player player = bukkitPlayer.getPlayer();
            Plot plot = ThemedBuildManager.getPlot(player.getLocation());
            if(plot!=null) {
                Bukkit.getOnlinePlayers().stream().filter(search -> predicate.test(search.getUniqueId(),plot))
                        .forEach(element->builder.suggest(element.getName()));
                return builder.buildFuture();
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("Eriol_Eandur");
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

}
