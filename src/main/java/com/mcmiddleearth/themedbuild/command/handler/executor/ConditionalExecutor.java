package com.mcmiddleearth.themedbuild.command.handler.executor;

import com.mcmiddleearth.themedbuild.Messages;
import com.mcmiddleearth.themedbuild.ThemedBuildPlugin;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class ConditionalExecutor {

    private List<Condition> conditions;

    private Player player;

    public ConditionalExecutor(Player player) {
        conditions = new LinkedList<>();
        this.player = player;
    }

    public ConditionalExecutor addCondition(boolean fulfilled, String errorMessage, String... messageArguments) {
        conditions.add(new Condition(fulfilled, errorMessage, messageArguments));
        return this;
    }

    public void execute(IExecutor executor, String successMessage, String... messageArguments) {
        for(Condition condition: conditions) {
            if(!condition.fulfilled) {
                if(successMessage!=null) {
                    sendError(condition.errorMessage, condition.messageArguments);
                }
                return;
            }
        }
        executor.execute();
        if(successMessage!=null) {
            sendSuccess(successMessage, messageArguments);
        }
    }

    private void sendSuccess(String messageNode, String... replacements) {
        ThemedBuildPlugin.messageUtil.fancyMessage().addSimple(Messages.get(messageNode,replacements)).send(player);
    }

    private void sendError(String messageNode, String... replacements) {
        ThemedBuildPlugin.messageUtil.fancyErrorMessage().addSimple(Messages.get(messageNode,replacements)).send(player);
    }

    private record Condition(boolean fulfilled, String errorMessage, String... messageArguments) {}

}
