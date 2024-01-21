package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractIntegerSuggestionListArgumentType;

import java.util.Collection;

public class OwnedPlotNumberArgument extends AbstractIntegerSuggestionListArgumentType {

    @Override
    protected Collection<String> getSuggestions() {
        return null;
    }

    @Override
    public String getTooltip() {
        return super.getTooltip();
    }
}
