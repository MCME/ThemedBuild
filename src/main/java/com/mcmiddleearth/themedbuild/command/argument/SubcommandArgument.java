package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractStringSuggestionListArgumentType;
import com.mcmiddleearth.command.argument.HelpfulArgumentType;
import com.mcmiddleearth.command.sender.McmeCommandSender;
import com.mcmiddleearth.themedbuild.command.handler.HelpHandler;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.enginehub.piston.util.HelpGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class SubcommandArgument implements HelpfulArgumentType, ArgumentType<String> {

    private String tooltip = "Any subcommand of command /theme";

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        HelpHandler.getSubcommands(context).forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("toplot","warp","info");
    }
}
