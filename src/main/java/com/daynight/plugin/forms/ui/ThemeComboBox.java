package com.daynight.plugin.forms.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;

import javax.swing.*;

public class ThemeComboBox extends ComboBox<UIManager.LookAndFeelInfo> {

    private final static ListCellRendererWrapper DEFAULT_RENDERER = new ListCellRendererWrapper<UIManager.LookAndFeelInfo>() {
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
