package com.daynight.plugin.utils;

import static com.intellij.openapi.editor.colors.EditorColorsManager.DEFAULT_SCHEME_NAME;

import com.daynight.plugin.state.PluginPropsState;
import com.intellij.ide.ui.LafManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.EditorColorsManagerImpl;
import lombok.experimental.UtilityClass;

import javax.swing.*;

@UtilityClass
public class ColorUtils {

    public static EditorColorsScheme getSchemeForCurrentTime(PluginPropsState state) {
        String schemeName = state.getColorSchemeNameForCurrentTime();
        return getSchemeForName(schemeName);
    }

    public static EditorColorsScheme getSchemeForName(String schemeName) {
        EditorColorsManagerImpl colorsManager = (EditorColorsManagerImpl) EditorColorsManager.getInstance();
        return schemeName != null ? colorsManager.getScheme(schemeName) : colorsManager.getScheme(DEFAULT_SCHEME_NAME);
    }

    public static UIManager.LookAndFeelInfo getLaFForCurrentTime(PluginPropsState state) {
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
