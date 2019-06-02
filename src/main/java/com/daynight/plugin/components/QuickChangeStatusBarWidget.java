package com.daynight.plugin.components;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class QuickChangeStatusBarWidget extends EditorBasedWidget {

    protected QuickChangeStatusBarWidget(@NotNull Project project) {
        super(project);
    }

    @NotNull
    @Override
    public String ID() {
        return "QuickChangeStatusBarWidget";
    }

    @Nullable
    @Override
    public StatusBarWidget.WidgetPresentation getPresentation(@NotNull PlatformType platformType) {
        return new WidgetPresentation();
    }

    private static class WidgetPresentation implements StatusBarWidget.IconPresentation{
        @NotNull
        @Override
        public Icon getIcon() {
            return IconLoader.getIcon("icon/dayAndNight.png");
        }

        @Nullable
        @Override
        public String getTooltipText() {
            return "";
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
}
