package com.daynight.plugin.configuration;

import com.daynight.plugin.forms.DayNightConfigurableGUI;
import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.ScheduledTasksService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DayNightConfigurable implements SearchableConfigurable {

    final PluginPropertiesStateService propertiesStateService = PluginPropertiesStateService.getInstance();
    final ScheduledTasksService tasksService = ScheduledTasksService.getInstance();

    DayNightConfigurableGUI configGUI;

    @NotNull
    public String getId() {
        return "preference.DayNightConfigurable";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Day And Night";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        configGUI = new DayNightConfigurableGUI();
        configGUI.setAllSettingComponentsEnabled(propertiesStateService.getState().isEnabled());
        configGUI.setSchemePickersPanelEnabled(propertiesStateService.getState().isSchemePickEnabled());
        return configGUI.getRootPanel();
    }

    @Override
    public void disposeUIResources() {
        configGUI = null;
    }

    @Override
    public boolean isModified() {
        return configGUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        configGUI.applyChanges();
        PluginPropsState state = propertiesStateService.getState();

        tasksService.cancelTasksIfExists();
        if (state.isEnabled()) {
            tasksService.submitTasksIfNeeded();
            DataContext dataContext = DataManager.getInstance().getDataContext(configGUI.getRootPanel());
            AnActionEvent event = AnActionEvent.createFromDataContext(ActionPlaces.UNKNOWN, null, dataContext);
            ActionManager.getInstance().getAction("DayNightChangeColor").actionPerformed(event);
        }
    }

    @Override
    public void reset() {
        configGUI.resetChanges();
    }
}
