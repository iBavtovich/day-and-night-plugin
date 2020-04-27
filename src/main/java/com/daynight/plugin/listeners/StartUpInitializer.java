package com.daynight.plugin.listeners;

import com.daynight.plugin.services.ScheduledTasksService;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StartUpInitializer implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();
        scheduledTasksService.submitTasksIfNeeded();
        changeLokAndFeel();
    }

    private void changeLokAndFeel() {
        ActionManager instance = ActionManager.getInstance();
        AnAction action = instance.getAction("DayNightChangeColor");
        ApplicationManager.getApplication().invokeLater(() -> action.actionPerformed(null));
    }
}
