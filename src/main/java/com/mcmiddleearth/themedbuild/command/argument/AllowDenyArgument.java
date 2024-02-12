package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractSuggestionListArgumentType;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Arrays;
import java.util.Collection;

public class AllowDenyArgument extends AbstractSuggestionListArgumentType<Boolean> {

    @Override
    protected Collection<String> getSuggestions() {
        return Arrays.asList("allow", "deny");
    }

    @Override
    public Boolean parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if(input.equalsIgnoreCase("allow")) {
            return true;
        } else if(input.equalsIgnoreCase("deny")) {
            return false;
        }
        throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage("Failed parsing of Allow/Deny argument")),
                new LiteralMessage("input must be either 'allow' or 'deny'"));
    }

}
