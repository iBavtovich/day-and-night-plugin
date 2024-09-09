package com.daynight.plugin.utils;

import com.daynight.plugin.state.PluginPropsState;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;

import java.util.Iterator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ColorUtils {

    public static EditorColorsScheme getSchemeForCurrentTime(PluginPropsState state) {
        String schemeName = state.getColorSchemeNameForCurrentTime();
        return getSchemeForName(schemeName);
    }

    public static EditorColorsScheme getSchemeForName(String schemeName) {
        EditorColorsManager colorsManager = EditorColorsManager.getInstance();
        return schemeName != null ? colorsManager.getScheme(schemeName)
                : colorsManager.getScheme(EditorColorsManager.getDefaultSchemeName());
    }

    public static UIThemeLookAndFeelInfo getLaFForCurrentTime(PluginPropsState state) {
        String themeName = state.getThemeNameForCurrentTime();
        return getThemeByName(themeName);
    }

    public static UIThemeLookAndFeelInfo getThemeByName(String themeName) {
        LafManager lafManager = LafManager.getInstance();

        Iterator<UIThemeLookAndFeelInfo> installedThemesIterator = lafManager.getInstalledThemes().iterator();
        while (installedThemesIterator.hasNext()) {
            UIThemeLookAndFeelInfo lookAndFeel = installedThemesIterator.next();
            if (lookAndFeel.getName().equalsIgnoreCase(themeName)) {
                return lookAndFeel;
            }
        }
        return lafManager.getCurrentUIThemeLookAndFeel();
    }
}
