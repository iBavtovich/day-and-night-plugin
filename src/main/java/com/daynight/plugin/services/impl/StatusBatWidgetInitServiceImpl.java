package com.daynight.plugin.services.impl;

import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.StatusBatWidgetInitService;
import com.daynight.plugin.state.PluginPropsState;
import com.daynight.plugin.utils.TimeUtils;
import com.intellij.openapi.project.Project;

public class StatusBatWidgetInitServiceImpl implements StatusBatWidgetInitService {

    private WidgetState state;


    public StatusBatWidgetInitServiceImpl(Project project) {
        PluginPropsState config = PluginPropertiesStateService.getInstance().getState();
        updateStateAccordingToState(config);
    }

    public void updateStateAccordingToState(PluginPropsState config) {
        state = TimeUtils.isDayNow(config) ? WidgetState.DAY : WidgetState.NIGHT;
    }

    @Override
    public WidgetState getState() {
        return state;
    }

    @Override
    public void updateState(WidgetState newState) {
        state = newState;
    }
}
