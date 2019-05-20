package com.daynight.plugin.actions;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.daynight.plugin.components.PluginPropertiesComponent.State;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.ide.ui.laf.darcula.DarculaInstaller;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.EditorColorsManagerImpl;
import com.intellij.openapi.util.Ref;
import com.intellij.util.ui.UIUtil;

import javax.annotation.Nullable;
import javax.swing.*;

import static com.intellij.openapi.editor.colors.EditorColorsManager.DEFAULT_SCHEME_NAME;

public class ChangeIdeAppearanceAction extends AnAction {

    @Override
    public void actionPerformed(@Nullable AnActionEvent e) {
        State state = PluginPropertiesComponent.getInstance().getState();
        if (state.isEnabled()) {
            EditorColorsScheme schemeForSwitch = getSchemeForCurrentTime(state);
            UIManager.LookAndFeelInfo themeForSwitch = getLaFForCurrentTime(state);

            changeLaFIfNecessary(themeForSwitch, schemeForSwitch);
            if (!state.isSchemePickDisabled()) {
                SwingUtilities.invokeLater(() -> EditorColorsManager.getInstance().setGlobalScheme(schemeForSwitch));
            }
        }
    }

    private EditorColorsScheme getSchemeForCurrentTime(State state) {
        EditorColorsManagerImpl colorsManager = (EditorColorsManagerImpl) EditorColorsManager.getInstance();
        String schemeName = state.getColorSchemeNameForCurrentTime();
        return schemeName != null ? colorsManager.getScheme(schemeName) : colorsManager.getScheme(DEFAULT_SCHEME_NAME);
    }

    private UIManager.LookAndFeelInfo getLaFForCurrentTime(State state) {
        LafManager lafManager = LafManager.getInstance();
        UIManager.LookAndFeelInfo[] installedLookAndFeels = lafManager.getInstalledLookAndFeels();
        String themeName = state.getThemeNameForCurrentTime();

        for (UIManager.LookAndFeelInfo lookAndFeel : installedLookAndFeels) {
            if (lookAndFeel.getName().equals(themeName)) {
                return lookAndFeel;
            }
        }

        return lafManager.getCurrentLookAndFeel();
    }

    private static void changeLaFIfNecessary(UIManager.LookAndFeelInfo themeForSwitch, EditorColorsScheme schemeForSwitch) {
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
    }
}
