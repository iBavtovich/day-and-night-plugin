package com.daynight.plugin.services;

import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface StatusBatWidgetInitService {
    static StatusBatWidgetInitService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, StatusBatWidgetInitService.class);
    }

    WidgetState getState();

    void updateState(WidgetState newState);

    void updateStateAccordingToState(PluginPropsState state);

    enum WidgetState {
        DAY,
        NIGHT
    }
}
