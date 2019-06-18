package com.daynight.plugin.actions;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.daynight.plugin.components.PluginPropertiesComponent.State;
import com.daynight.plugin.components.WidgetInitComponent;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.ide.ui.laf.darcula.DarculaInstaller;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.util.Ref;
import com.intellij.util.ui.UIUtil;

import javax.annotation.Nullable;
import javax.swing.*;

import static com.daynight.plugin.utils.ColorUtils.getLaFForCurrentTime;
import static com.daynight.plugin.utils.ColorUtils.getSchemeForCurrentTime;

public class ChangeIdeAppearanceAction extends AnAction {

    @Override
    public void actionPerformed(@Nullable AnActionEvent e) {
        State state = PluginPropertiesComponent.getInstance().getState();
        if (e != null) {
            WidgetInitComponent component = e.getProject().getComponent(WidgetInitComponent.class);
            component.setStateAccordindToTime(state);
        }

        if (state.isEnabled()) {
            EditorColorsScheme schemeForSwitch = getSchemeForCurrentTime(state);
            UIManager.LookAndFeelInfo themeForSwitch = getLaFForCurrentTime(state);

            changeLaFIfNecessary(themeForSwitch, schemeForSwitch, state);
        }
    }

    static void changeLaFIfNecessary(UIManager.LookAndFeelInfo themeForSwitch, EditorColorsScheme schemeForSwitch, State state) {
        final LafManager lafManager = LafManager.getInstance();
        boolean wasDark = UIUtil.isUnderDarcula();

        lafManager.setCurrentLookAndFeel(themeForSwitch);

        Ref<Boolean> updated = Ref.create(false);
        LafManagerListener listener = s -> updated.set(true);
        lafManager.addLafManagerListener(listener);
        try {
            if (UIUtil.isUnderDarcula()) {
                DarculaInstaller.install();
            } else if (wasDark) {
                DarculaInstaller.uninstall();
            }
        } finally {
            lafManager.removeLafManagerListener(listener);
            if (!updated.get()) {
                lafManager.updateUI();
            }
        }

        if (state.isSchemePickEnabled()) {
            SwingUtilities.invokeLater(() -> EditorColorsManager.getInstance().setGlobalScheme(schemeForSwitch));
        }
    }
}
