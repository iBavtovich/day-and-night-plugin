package com.daynight.plugin.forms.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.SimpleListCellRenderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static java.util.Objects.isNull;

public class ThemeComboBox extends ComboBox<UIManager.LookAndFeelInfo> {
    private static final SimpleListCellRenderer<UIManager.LookAndFeelInfo> DEFAULT_RENDERER = new SimpleListCellRenderer<UIManager.LookAndFeelInfo>() {
        @Override
        public void customize(@NotNull JList list, UIManager.LookAndFeelInfo value, int index, boolean selected, boolean hasFocus) {
            setText(value.getName());
        }
    };

    public ThemeComboBox(UIManager.LookAndFeelInfo[] items){
        super(items);
        super.setRenderer(DEFAULT_RENDERER);
    }

    public UIManager.LookAndFeelInfo getSelectedTheme() {
        return (UIManager.LookAndFeelInfo) super.getSelectedItem();
    }

    public void setSelectedTheme(UIManager.LookAndFeelInfo theme) {
        setSelectedItem(theme);
    }

    @Nullable
    public String getSelectedThemeName() {
        UIManager.LookAndFeelInfo selectedTheme = getSelectedTheme();
        return isNull(selectedTheme) ? null : selectedTheme.getName();
    }
}
