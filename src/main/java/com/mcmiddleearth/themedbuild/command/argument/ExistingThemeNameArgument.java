package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractStringSuggestionListArgumentType;

import java.util.Collection;

public class ExistingThemeNameArgument extends AbstractStringSuggestionListArgumentType {

    @Override
    protected Collection<String> getSuggestions() {
        return null;
    }

    @Override
    public String getTooltip() {
        return super.getTooltip();
    }
}
