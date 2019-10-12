package com.daynight.plugin.components;

import com.daynight.plugin.actions.ChangeIdeAppearanceAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static com.daynight.plugin.components.WidgetInitComponent.WidgetState.DAY;
import static com.daynight.plugin.components.WidgetInitComponent.WidgetState.NIGHT;
import static com.daynight.plugin.utils.ColorUtils.getLookAndFeelInfoForName;
import static com.daynight.plugin.utils.ColorUtils.getSchemeForName;

public class QuickChangeStatusBarWidget implements StatusBarWidget.Multiframe, StatusBarWidget.IconPresentation {

    private Project myProject;
    private Consumer<MouseEvent> clickConsumer;

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
            PluginPropertiesComponent.State state = PluginPropertiesComponent.getInstance().getState();
            WidgetInitComponent widget = myProject.getComponent(WidgetInitComponent.class);

            String themeForUpdate = null;
            String schemeForUpdate = null;

            switch (widget.getState()) {
                case DAY:
                    schemeForUpdate = state.getNightSchemeName();
                    themeForUpdate = state.getNightThemeName();
                    widget.setState(NIGHT);
                    break;

                case NIGHT:
                    schemeForUpdate = state.getDaySchemeName();
                    themeForUpdate = state.getDayThemeName();
                    widget.setState(DAY);
                    break;
            }

            if (themeForUpdate != null) {
                ChangeIdeAppearanceAction.changeLaFIfNecessary(getLookAndFeelInfoForName(themeForUpdate), getSchemeForName(schemeForUpdate), state);
            }
        }
    }
}
