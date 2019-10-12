package com.daynight.plugin.components;

import com.daynight.plugin.utils.TimeUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.awt.*;

import static com.daynight.plugin.components.WidgetInitComponent.WidgetState.DAY;
import static com.daynight.plugin.components.WidgetInitComponent.WidgetState.NIGHT;

@Setter
@Getter
@RequiredArgsConstructor
public class WidgetInitComponent implements ProjectComponent {

    private WidgetState state;

    private final Project myProject;

    public static WidgetInitComponent getInstance() {
        Project project = getProject();
        if (project == null) return null;
        return project.getComponent(WidgetInitComponent.class);
    }

    private static Project getProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        for (Project project : projects) {
            Window window = WindowManager.getInstance().suggestParentWindow(project);
            if (window != null && window.isActive()) {
                activeProject = project;
            }
        }
        return activeProject;
    }

    @Override
    public void projectOpened() {
        PluginPropertiesComponent.State config = PluginPropertiesComponent.getInstance().getState();
        setStateAccordingToTime(config);

        ApplicationManager.getApplication().invokeLater(() -> {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(myProject);

            if (statusBar != null) {
                QuickChangeStatusBarWidget widget = new QuickChangeStatusBarWidget(myProject);
                statusBar.addWidget(widget);
                Disposer.register(myProject, () -> statusBar.removeWidget(widget.ID()));
            }
        });

    }

    public void setStateAccordingToTime(PluginPropertiesComponent.State config) {
        state = TimeUtils.isDayNow(config) ? DAY : NIGHT;
    }

    public enum WidgetState {
        DAY,
        NIGHT
    }
}
