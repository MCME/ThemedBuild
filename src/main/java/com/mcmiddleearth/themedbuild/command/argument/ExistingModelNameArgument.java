package com.mcmiddleearth.themedbuild.command.argument;

import com.mcmiddleearth.command.argument.AbstractStringSuggestionListArgumentType;
import com.mcmiddleearth.themedbuild.data.PlotModel;
import com.mcmiddleearth.themedbuild.data.PlotModelManager;

import java.util.Collection;
import java.util.stream.Collectors;

public class ExistingModelNameArgument extends AbstractStringSuggestionListArgumentType {

    public ExistingModelNameArgument() {
        setTooltip("Name of an existing plot model.");
    }

    @Override
    protected Collection<String> getSuggestions() {
        return PlotModelManager.getModels().stream().map(PlotModel::getName).collect(Collectors.toList());
    }

}
