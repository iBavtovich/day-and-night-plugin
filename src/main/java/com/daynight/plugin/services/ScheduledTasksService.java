package com.daynight.plugin.services;

import com.intellij.openapi.application.ApplicationManager;

public interface ScheduledTasksService {
    static ScheduledTasksService getInstance() {
        return ApplicationManager.getApplication().getService(ScheduledTasksService.class);
    }

    void submitTasksIfNeeded();

    void cancelTask();
}
