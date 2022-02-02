package com.daynight.plugin.components;

import static com.daynight.plugin.services.StatusBatWidgetInitService.WidgetState.DAY;
import static com.daynight.plugin.services.StatusBatWidgetInitService.WidgetState.NIGHT;
import static com.daynight.plugin.utils.ColorUtils.getLookAndFeelInfoForName;
import static com.daynight.plugin.utils.ColorUtils.getSchemeForName;

import com.daynight.plugin.actions.ChangeIdeAppearanceAction;
import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.StatusBatWidgetInitService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

import javax.swing.*;

public class QuickChangeStatusBarWidget implements StatusBarWidget.Multiframe, StatusBarWidget.IconPresentation {

    private final Consumer<MouseEvent> clickConsumer;
    private Project myProject;

    public QuickChangeStatusBarWidget(@NotNull Project project) {
        myProject = project;
        clickConsumer = new QuickSwitchEvent();
    }

    @NotNull
    @Override
    public String ID() {
        return "QuickChangeStatusBarWidget";
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    public void dispose() {
        myProject = null;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/icon/dayAndNight.png", QuickChangeStatusBarWidget.class);
    }

    @Override
    public StatusBarWidget copy() {
        return new QuickChangeStatusBarWidget(myProject);
    }

    @NotNull
    @Override
    public String getTooltipText() {
        return "Change IDE appearance";
    }

    @NotNull
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        return clickConsumer;
    }

    public class QuickSwitchEvent implements Consumer<MouseEvent> {

        @Override
        public void consume(MouseEvent mouseEvent) {
            PluginPropsState state = PluginPropertiesStateService.getInstance().getState();
            StatusBatWidgetInitService widget = StatusBatWidgetInitService.getInstance(myProject);

            String themeForUpdate = null;
            String schemeForUpdate = null;

            switch (widget.getState()) {
                case DAY:
                    schemeForUpdate = state.getNightSchemeName();
                    themeForUpdate = state.getNightThemeName();
                    widget.updateState(NIGHT);
                    break;

                case NIGHT:
                    schemeForUpdate = state.getDaySchemeName();
                    themeForUpdate = state.getDayThemeName();
                    widget.updateState(DAY);
                    break;
            }

            if (themeForUpdate != null) {
                ChangeIdeAppearanceAction.changeLaFIfNecessary(getLookAndFeelInfoForName(themeForUpdate), getSchemeForName(schemeForUpdate), state);
            }
        }
    }
}
