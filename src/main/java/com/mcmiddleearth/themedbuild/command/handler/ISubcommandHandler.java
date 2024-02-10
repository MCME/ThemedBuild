package com.mcmiddleearth.themedbuild.command.handler;

import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;

public interface ISubcommandHandler extends ICommandHandler {

    void addCommandTree(HelpfulLiteralBuilder helpfulLiteralBuilder);

}
