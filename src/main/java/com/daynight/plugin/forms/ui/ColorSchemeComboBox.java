package com.daynight.plugin.forms.ui;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.ui.ComboBox;

public class ColorSchemeComboBox extends ComboBox<EditorColorsScheme> {

    public ColorSchemeComboBox(EditorColorsScheme[] items) {
        super(items);
    }

    public EditorColorsScheme getSelectedScheme() {
        return (EditorColorsScheme) super.getSelectedItem();
    }

    public void setSelectedScheme(EditorColorsScheme scheme) {
        setSelectedItem(scheme);
    }

    public String getSelectedSchemeName() {
        return getSelectedScheme().getName();
    }

}
