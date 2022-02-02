package com.daynight.plugin.services;

import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import org.jetbrains.annotations.NotNull;

public interface StatusBatWidgetInitService {
    static StatusBatWidgetInitService getInstance(@NotNull Project project) {
        return project.getService(StatusBatWidgetInitService.class);
    }

    WidgetState getState();

    void updateState(WidgetState newState);

    void updateStateAccordingToState(PluginPropsState state);

    enum WidgetState {
        DAY,
        NIGHT
    }
}
