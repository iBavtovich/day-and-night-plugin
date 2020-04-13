package com.daynight.plugin.listeners;

import com.daynight.plugin.services.ScheduledTasksService;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StartUpInitializer implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();
            scheduledTasksService.submitTasksIfNeeded();
            changeLookAndFeelIfNeeded();
        });
    }

    private void changeLookAndFeelIfNeeded() {
        AnAction dayNightChangeLaF = ActionManager.getInstance().getAction("DayNightChangeColor");
        dayNightChangeLaF.actionPerformed(null);
    }
}
