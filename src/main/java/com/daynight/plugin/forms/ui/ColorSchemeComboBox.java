package com.daynight.plugin.forms.ui;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.ui.ComboBox;

import org.jetbrains.annotations.Nullable;

import static java.util.Objects.isNull;

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

    @Nullable
    public String getSelectedSchemeName() {
        EditorColorsScheme selectedScheme = getSelectedScheme();
        return isNull(selectedScheme) ? null : selectedScheme.getName();
    }
}
