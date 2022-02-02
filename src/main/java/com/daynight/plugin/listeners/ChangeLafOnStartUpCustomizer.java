package com.daynight.plugin.listeners;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.impl.ActionConfigurationCustomizer;
import com.intellij.openapi.application.ApplicationManager;

import org.jetbrains.annotations.NotNull;

/**
 * Will be executed once action manager is initialized and ready.
 */
public class ChangeLafOnStartUpCustomizer implements ActionConfigurationCustomizer {

    @Override
    public void customize(@NotNull ActionManager actionManager) {
        ApplicationManager.getApplication().invokeLater(() -> {
            AnAction action = actionManager.getAction("DayNightChangeColor");
            action.actionPerformed(null);
        });
    }
}
