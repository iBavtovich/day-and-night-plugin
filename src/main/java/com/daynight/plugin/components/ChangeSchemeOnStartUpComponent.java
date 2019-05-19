package com.daynight.plugin.components;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeSchemeOnStartUpComponent {

    final ScheduledTasksComponent tasksComponent;
    public ChangeSchemeOnStartUpComponent(ScheduledTasksComponent tasksComponent) {
        this.tasksComponent = tasksComponent;
        changeLookAndFeelIfNeeded();
        tasksComponent.submitTasksIfNeeded();
    }

    private void changeLookAndFeelIfNeeded() {
        AnAction dayNightChangeLaF = ActionManager.getInstance().getAction("DayNightChangeColor");
        dayNightChangeLaF.actionPerformed(null);
    }
}
