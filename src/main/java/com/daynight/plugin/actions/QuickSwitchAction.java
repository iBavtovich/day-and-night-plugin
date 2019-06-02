package com.daynight.plugin.actions;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.daynight.plugin.components.WidgetInitComponent;
import com.daynight.plugin.utils.ColorUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import static com.daynight.plugin.components.WidgetInitComponent.WidgetState.DAY;
import static com.daynight.plugin.components.WidgetInitComponent.WidgetState.NIGHT;
import static com.daynight.plugin.utils.ColorUtils.*;

public class QuickSwitchAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PluginPropertiesComponent.State state = PluginPropertiesComponent.getInstance().getState();
        WidgetInitComponent widget = WidgetInitComponent.getInstance();

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
