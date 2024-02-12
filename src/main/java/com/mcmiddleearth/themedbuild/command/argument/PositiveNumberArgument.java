package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractIntegerSuggestionListArgumentType;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PositiveNumberArgument extends AbstractIntegerSuggestionListArgumentType {

    public PositiveNumberArgument() {
        setTooltip("Any positive number.");
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        String o = reader.readUnquotedString();
        try {
            int page = Integer.parseInt(o);
            if(page>0) {
                return page;
            }
        } catch(NumberFormatException ignore){}
        throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing of positive number argument")),
                new LiteralMessage("value must be an integer > 0"));
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("1","2","3");
    }

    @Override
    protected Collection<String> getSuggestions() {
        List<String> suggestions = new LinkedList<>();
        for(int i = 0; i < 100; i++) {
            suggestions.add(""+i);
        }
        return suggestions;
    }

}
