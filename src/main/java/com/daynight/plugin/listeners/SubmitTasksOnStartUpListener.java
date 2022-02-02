package com.daynight.plugin.listeners;

import com.daynight.plugin.services.ScheduledTasksService;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Initializer to schedule task for switching theme/scheme for the next period.
 */
public class SubmitTasksOnStartUpListener implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        ScheduledTasksService scheduledTasksService = ScheduledTasksService.getInstance();
        scheduledTasksService.submitTasksIfNeeded();
    }
}
