package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.HelpfulArgumentType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class AvailableThemedbuildNameArgument implements HelpfulArgumentType, ArgumentType<String> {

    @Override
    public void setTooltip(String s) {

    }

    @Override
    public String getTooltip() {
        return HelpfulArgumentType.super.getTooltip();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ArgumentType.super.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}