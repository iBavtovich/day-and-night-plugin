package com.daynight.plugin.services.impl;

import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@State(
        name = "PluginPropertiesComponent",
        storages = @Storage("dayNightSettings.xml"),
        reloadable = false
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PluginPropertiesStateServiceImpl implements PluginPropertiesStateService {

    private PluginPropsState myState = new PluginPropsState();

    @Override
    @NotNull
    public PluginPropsState getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull PluginPropsState state) {
        myState = state;
    }

    @NotNull
    public static PluginPropertiesStateService getInstance() {
        return ApplicationManager.getApplication().getComponent(PluginPropertiesStateService.class);
    }
}