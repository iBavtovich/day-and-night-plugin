package com.daynight.plugin.listeners;

import com.daynight.plugin.actions.ChangeIdeAppearanceAction;
import com.daynight.plugin.services.ScheduledTasksService;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

import org.jetbrains.annotations.NotNull;

import static com.daynight.plugin.components.QuickChangeStatusBarWidget.WIDGET_ID;

public class LoadListener implements DynamicPluginListener {
    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        loadWidget();
        changeLAFOnLoad();
        submitTasks();
    }

    @Override
    public void pluginUnloaded(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        // No-op.
    }

    private static void submitTasks() {
        ScheduledTasksService.getInstance().submitTasksIfNeeded();
    }

    private static void changeLAFOnLoad() {
        ChangeIdeAppearanceAction service = ApplicationManager.getApplication()
                .getService(ChangeIdeAppearanceAction.class);
        if (service != null) {
            service.actionPerformed(null);
        }
    }

    private static void loadWidget() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            if (statusBar != null) {
                statusBar.updateWidget(WIDGET_ID);
            }
        }
    }
}
