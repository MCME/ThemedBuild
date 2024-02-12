package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractStringSuggestionListArgumentType;
import com.mcmiddleearth.themedbuild.data.PlotModel;
import com.mcmiddleearth.themedbuild.data.PlotModelManager;
import com.mcmiddleearth.themedbuild.data.ThemedBuild;
import com.mcmiddleearth.themedbuild.data.ThemedBuildManager;

import java.util.Collection;
import java.util.stream.Collectors;

public class ExistingThemeNameArgument extends AbstractStringSuggestionListArgumentType {

    public ExistingThemeNameArgument() {
        setTooltip("Name of an existing themed-build.");
    }

    @Override
    protected Collection<String> getSuggestions() {
        return ThemedBuildManager.getThemedBuilds().stream().map(ThemedBuild::getName).collect(Collectors.toList());
    }
}
