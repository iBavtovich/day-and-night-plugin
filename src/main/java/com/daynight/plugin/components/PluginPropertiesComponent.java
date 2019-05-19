package com.daynight.plugin.components;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@State(
        name = "PluginPropertiesComponent",
        storages = @Storage("dayNightSettings.xml"),
        reloadable = false
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PluginPropertiesComponent implements PersistentStateComponent<PluginPropertiesComponent.State> {

    State myState = new State();

    @Override
    @NotNull
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    @NotNull
    public static PluginPropertiesComponent getInstance() {
        return ServiceManager.getService(PluginPropertiesComponent.class);
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class State {

        boolean isEnabled;
        boolean schemePickEnabled;
        // Time in both cases is the number of minutes from midnight (e.g. 1 -> 0:01AM, 60 -> 1:00AM and so on)
        int dayStartTime;
        int nightStartTime;

        String daySchemeName;
        String nightSchemeName;

        String dayThemeName;
        String nightThemeName;
    }
}
