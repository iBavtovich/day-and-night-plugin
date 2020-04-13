package com.daynight.plugin.services;

import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;

public interface PluginPropertiesStateService extends PersistentStateComponent<PluginPropsState> {
    static PluginPropertiesStateService getInstance() {
        return ServiceManager.getService(PluginPropertiesStateService.class);
    }

    PluginPropsState getState();
}
