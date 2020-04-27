package com.daynight.plugin.actions;

import static com.daynight.plugin.utils.ColorUtils.getLaFForCurrentTime;
import static com.daynight.plugin.utils.ColorUtils.getSchemeForCurrentTime;

import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.StatusBatWidgetInitService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.ide.actions.QuickChangeLookAndFeel;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;

import javax.annotation.Nullable;
import javax.swing.*;

public class ChangeIdeAppearanceAction extends AnAction {

    @Override
    public void actionPerformed(@Nullable AnActionEvent e) {
        ApplicationManager.getApplication().invokeLater(() -> {
            PluginPropsState state = PluginPropertiesStateService.getInstance().getState();
            if (e != null && e.getProject() != null) {
                StatusBatWidgetInitService widgetInitService = StatusBatWidgetInitService.getInstance(e.getProject());
                widgetInitService.updateStateAccordingToState(state);
            }

            if (state.isEnabled()) {
                EditorColorsScheme schemeForSwitch = getSchemeForCurrentTime(state);
                UIManager.LookAndFeelInfo themeForSwitch = getLaFForCurrentTime(state);

                changeLaFIfNecessary(themeForSwitch, schemeForSwitch, state);
            }
        });
    }

    public static void changeLaFIfNecessary(UIManager.LookAndFeelInfo themeForSwitch,
                                            EditorColorsScheme schemeForSwitch,
                                            PluginPropsState state) {
        final LafManager lafManager = LafManager.getInstance();
        QuickChangeLookAndFeel.switchLafAndUpdateUI(lafManager, themeForSwitch, true);
        EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
        if (state.isSchemePickEnabled()) {
            SwingUtilities.invokeLater(() -> editorColorsManager.setGlobalScheme(schemeForSwitch));
        } else /* Set default for chosen theme */ {
            SwingUtilities.invokeLater(() -> editorColorsManager.setGlobalScheme(editorColorsManager.getSchemeForCurrentUITheme()));
        }
    }
}
