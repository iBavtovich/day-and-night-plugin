package com.daynight.plugin.components;

import com.daynight.plugin.actions.ChangeIdeAppearanceAction;
import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.StatusBatWidgetInitService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import com.intellij.util.ReflectionUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.Icon;

import static com.daynight.plugin.services.StatusBatWidgetInitService.WidgetState.DAY;
import static com.daynight.plugin.services.StatusBatWidgetInitService.WidgetState.NIGHT;
import static com.daynight.plugin.utils.ColorUtils.getSchemeForName;
import static com.daynight.plugin.utils.ColorUtils.getThemeByName;

public class QuickChangeStatusBarWidget implements StatusBarWidget, StatusBarWidget.IconPresentation {
    private static final Icon QUICK_SWITCH_ACTION = IconLoader.getIcon("/icon/dayAndNight.png",
            Objects.requireNonNull(ReflectionUtil.getGrandCallerClass()));

    public static final String WIDGET_ID = "QuickChange";

    private final Project myProject;
    private final Consumer<MouseEvent> clickConsumer;

    public QuickChangeStatusBarWidget(@NotNull Project project) {
        this.myProject = project;
        this.clickConsumer = new QuickSwitchEvent();
    }

    @Override
    public @NotNull String ID() {
        return WIDGET_ID;
    }

    @Override
    public @Nullable WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    public @Nullable Icon getIcon() {
        return QUICK_SWITCH_ACTION;
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        return clickConsumer;
    }

    @Override
    public @Nullable String getTooltipText() {
        return "Change IDE appearance";
    }

    public class QuickSwitchEvent implements Consumer<MouseEvent> {

        @Override
        public void consume(MouseEvent mouseEvent) {
            PluginPropsState state = PluginPropertiesStateService.getInstance().getState();
            StatusBatWidgetInitService widget = StatusBatWidgetInitService.getInstance(myProject);

            String themeForUpdate = null;
            String schemeForUpdate = null;

            switch (widget.getState()) {
                case DAY -> {
                    schemeForUpdate = state.getNightSchemeName();
                    themeForUpdate = state.getNightThemeName();
                    widget.updateState(NIGHT);
                }
                case NIGHT -> {
                    schemeForUpdate = state.getDaySchemeName();
                    themeForUpdate = state.getDayThemeName();
                    widget.updateState(DAY);
                }
            }

            if (themeForUpdate != null) {
                ChangeIdeAppearanceAction.changeLaFIfNecessary(getThemeByName(themeForUpdate), getSchemeForName(schemeForUpdate), state);
            }
        }
    }
}
