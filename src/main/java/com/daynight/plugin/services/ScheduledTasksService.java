package com.daynight.plugin.services;

import com.intellij.openapi.components.ServiceManager;

public interface ScheduledTasksService {
    static ScheduledTasksService getInstance() {
        return ServiceManager.getService(ScheduledTasksService.class);
    }

    void submitTasksIfNeeded();

    void cancelTasksIfExists();
}
