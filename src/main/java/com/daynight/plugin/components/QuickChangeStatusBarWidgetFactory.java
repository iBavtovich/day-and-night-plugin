package com.daynight.plugin.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import com.intellij.openapi.wm.impl.status.widget.StatusBarEditorBasedWidgetFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class QuickChangeStatusBarWidgetFactory implements StatusBarWidgetFactory {
    private static final String DISPLAY_NAME = "Change IDE appearance";
    private static final String WIDGET_ID = "QuickChange";

    @NotNull
    @Override
    public String getId() {
        return WIDGET_ID;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @NotNull
    @Override
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new QuickChangeStatusBarWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
        Disposer.dispose(widget);
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }
}
