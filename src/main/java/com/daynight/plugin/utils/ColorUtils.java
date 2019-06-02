package com.daynight.plugin.utils;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.EditorColorsManagerImpl;

import javax.swing.*;

import static com.intellij.openapi.editor.colors.EditorColorsManager.DEFAULT_SCHEME_NAME;

public class ColorUtils {

    public static EditorColorsScheme getSchemeForCurrentTime(PluginPropertiesComponent.State state) {
        String schemeName = state.getColorSchemeNameForCurrentTime();
        return getSchemeForName(schemeName);
    }

    public static EditorColorsScheme getSchemeForName(String schemeName) {
        EditorColorsManagerImpl colorsManager = (EditorColorsManagerImpl) EditorColorsManager.getInstance();
        return schemeName != null ? colorsManager.getScheme(schemeName) : colorsManager.getScheme(DEFAULT_SCHEME_NAME);
    }

    public static UIManager.LookAndFeelInfo getLaFForCurrentTime(PluginPropertiesComponent.State state) {
        String themeName = state.getThemeNameForCurrentTime();
        return getLookAndFeelInfoForName(themeName);
    }

    public static UIManager.LookAndFeelInfo getLookAndFeelInfoForName(String themeName) {
        LafManager lafManager = LafManager.getInstance();
        UIManager.LookAndFeelInfo[] installedLookAndFeels = lafManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo lookAndFeel : installedLookAndFeels) {
            if (lookAndFeel.getName().equals(themeName)) {
                return lookAndFeel;
            }
        }

        return lafManager.getCurrentLookAndFeel();
    }
}
