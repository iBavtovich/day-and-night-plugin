package com.daynight.plugin.configuration;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.daynight.plugin.components.ScheduledTasksComponent;
import com.daynight.plugin.forms.DayNightConfigurableGUI;
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

    final PluginPropertiesComponent propertiesComponent;
    final ScheduledTasksComponent tasksComponent;

    DayNightConfigurableGUI configGUI;

    @NotNull
    @Override
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
        configGUI.setAllSettingComponentsEnabled(propertiesComponent.getState().isEnabled());
        configGUI.setSchemePickersPanelEnabled(propertiesComponent.getState().isSchemePickEnabled());
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
        PluginPropertiesComponent.State state = propertiesComponent.getState();

        tasksComponent.cancelTasksIfExists();
        if (state.isEnabled()) {
            tasksComponent.submitTasksIfNeeded();
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
