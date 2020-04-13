package com.daynight.plugin.forms.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SimpleListCellRenderer;

import javax.swing.*;

public class ThemeComboBox extends ComboBox<UIManager.LookAndFeelInfo> {

    private final static SimpleListCellRenderer DEFAULT_RENDERER = new SimpleListCellRenderer<UIManager.LookAndFeelInfo>() {
        @Override
        public void customize(JList list, UIManager.LookAndFeelInfo value, int index, boolean selected, boolean hasFocus) {
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

    public String getSelectedThemeName() {
        return getSelectedTheme().getName();
    }
}
