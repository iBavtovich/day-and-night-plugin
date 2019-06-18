package com.daynight.plugin.components;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class QuickChangeStatusBarWidget implements StatusBarWidget.Multiframe, StatusBarWidget.IconPresentation {

    private Project myProject;

    public QuickChangeStatusBarWidget(@NotNull Project project) {
        myProject = project;
    }

    @NotNull
    @Override
    public String ID() {
        return "QuickChangeStatusBarWidget";
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType type) {
        return this;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    public void dispose() {
        myProject = null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/icon/dayAndNight.png", QuickChangeStatusBarWidget.class);
    }

    @Override
    public StatusBarWidget copy() {
        return new QuickChangeStatusBarWidget(myProject);
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return "Change IDE appearance";
    }

    @Nullable
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        return mouseEvent -> {
            AnAction action = ActionManager.getInstance().getAction("DayAndNight.ManualSwitching");
            action.actionPerformed(null);
        };
    }
}
