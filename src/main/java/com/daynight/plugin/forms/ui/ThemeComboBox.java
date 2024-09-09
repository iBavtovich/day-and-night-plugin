package com.daynight.plugin.forms.ui;

import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.SimpleListCellRenderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JList;

import static java.util.Objects.isNull;

public class ThemeComboBox extends ComboBox<UIThemeLookAndFeelInfo> {
    private static final SimpleListCellRenderer<UIThemeLookAndFeelInfo> DEFAULT_RENDERER = new SimpleListCellRenderer<>() {
        @Override
        public void customize(@NotNull JList list, UIThemeLookAndFeelInfo value, int index, boolean selected, boolean hasFocus) {
            setText(value.getName());
        }
    };

    public ThemeComboBox(UIThemeLookAndFeelInfo[] items){
        super(items);
        super.setRenderer(DEFAULT_RENDERER);
    }

    public UIThemeLookAndFeelInfo getSelectedTheme() {
        return (UIThemeLookAndFeelInfo) super.getSelectedItem();
    }

    public void setSelectedTheme(UIThemeLookAndFeelInfo theme) {
        setSelectedItem(theme);
    }

    @Nullable
    public String getSelectedThemeName() {
        UIThemeLookAndFeelInfo selectedTheme = getSelectedTheme();
        return isNull(selectedTheme) ? null : selectedTheme.getName();
    }
}
