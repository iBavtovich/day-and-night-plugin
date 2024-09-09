package com.daynight.plugin.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class QuickChangeStatusBarWidgetFactory implements StatusBarWidgetFactory {
    private static final String DISPLAY_NAME = "Change IDE appearance";

    @NotNull
    @Override
    public String getId() {
        return QuickChangeStatusBarWidget.WIDGET_ID;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @NotNull
    @Override
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new QuickChangeStatusBarWidget(project);
    }
}