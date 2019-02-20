package com.daynight.plugin.actions;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.daynight.plugin.components.PluginPropertiesComponent.State;
import com.daynight.plugin.utils.TimeUtils;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.UITheme;
import com.intellij.ide.ui.laf.LafManagerImpl;
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo;
import com.intellij.ide.ui.laf.darcula.DarculaInstaller;
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.EditorColorsManagerImpl;
import com.intellij.openapi.options.SchemeManager;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.UIUtil;

import javax.annotation.Nullable;
import javax.swing.*;

public class ChangeColorSchemeAction extends AnAction {

    @Override
    public void actionPerformed(@Nullable AnActionEvent e) {
        State state = PluginPropertiesComponent.getInstance().getState();
        EditorColorsScheme schemeForSwitch = getSchemeForCurrentTime(state);

        changeColorSchemeIfNecessary(schemeForSwitch);
    }

    private EditorColorsScheme getSchemeForCurrentTime(State state) {
        EditorColorsManagerImpl colorsManager = (EditorColorsManagerImpl) EditorColorsManager.getInstance();
        if (state.isEnabled()) {
            if (TimeUtils.isDayNow(state)) {
                return colorsManager.getScheme(state.getDaySchemeName());
            } else {
                return colorsManager.getScheme(state.getNightSchemeName());
            }
        } else {
            return colorsManager.getGlobalScheme();
        }
    }

    private static void changeColorSchemeIfNecessary(EditorColorsScheme scheme) {
        final LafManager lafManager = LafManager.getInstance();
        boolean isDarkEditorTheme = ColorUtil.isDark(scheme.getDefaultBackground());

        UIManager.LookAndFeelInfo suitableLaf = null;
        String schemeName = SchemeManager.getDisplayName(scheme);
        for (UIManager.LookAndFeelInfo laf : lafManager.getInstalledLookAndFeels()) {
            if (laf instanceof UIThemeBasedLookAndFeelInfo &&
                    schemeName.equals(((UIThemeBasedLookAndFeelInfo) laf).getTheme().getEditorSchemeName())) {
                suitableLaf = laf;
                break;
            }
        }

        UIManager.LookAndFeelInfo currentLafInfo = lafManager.getCurrentLookAndFeel();
        UITheme theme = currentLafInfo instanceof UIThemeBasedLookAndFeelInfo ?
                ((UIThemeBasedLookAndFeelInfo) currentLafInfo).getTheme() : null;

        if (isDarkEditorTheme && (UIUtil.isUnderIntelliJLaF() || theme != null && !theme.isDark())) {
            lafManager.setCurrentLookAndFeel(suitableLaf != null ? suitableLaf : new DarculaLookAndFeelInfo());
            lafManager.updateUI();
            SwingUtilities.invokeLater(DarculaInstaller::install);
        } else if (!isDarkEditorTheme && (UIUtil.isUnderDarcula() || theme != null && theme.isDark())) {
            lafManager.setCurrentLookAndFeel(suitableLaf != null ? suitableLaf : ((LafManagerImpl) lafManager).getDefaultLaf());
            lafManager.updateUI();
            SwingUtilities.invokeLater(DarculaInstaller::uninstall);
        }
    }
}
