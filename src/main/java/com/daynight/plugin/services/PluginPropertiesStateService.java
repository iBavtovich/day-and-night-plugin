package com.daynight.plugin.services;

import com.daynight.plugin.state.PluginPropsState;
import com.intellij.ide.actionMacro.ActionMacroManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;

import org.jetbrains.annotations.NotNull;

public interface PluginPropertiesStateService extends PersistentStateComponent<PluginPropsState> {
    static PluginPropertiesStateService getInstance() {
        return ApplicationManager.getApplication().getService(PluginPropertiesStateService.class);
    }

    @NotNull PluginPropsState getState();
}
