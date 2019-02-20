package com.daynight.plugin.forms;

import com.daynight.plugin.components.PluginPropertiesComponent;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.EditorColorsManagerImpl;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalTime;

import static com.daynight.plugin.utils.TimeUtils.getLocalTimeFromMinutes;
import static com.daynight.plugin.utils.TimeUtils.getTimeInMinutes;
import static org.apache.commons.lang.StringUtils.isEmpty;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DayNightConfigurableGUI {
    JPanel rootPanel;
    JPanel settingsPanel;
    ComboBox daySchemePicker;
    ComboBox nightSchemePicker;
    TimePicker dayTimePicker;
    TimePicker nightTimePicker;
    JCheckBox changeSchNowCheckbox;

    PluginPropertiesComponent config;

    private void createUIComponents() {
        config = PluginPropertiesComponent.getInstance();
        PluginPropertiesComponent.State state = config.getState();

        settingsPanel = new JPanel();

        EditorColorsManagerImpl editorColorsManager = (EditorColorsManagerImpl) EditorColorsManager.getInstance();
        EditorColorsScheme[] allColorSchemes = editorColorsManager.getAllSchemes();

        daySchemePicker = new ComboBox(allColorSchemes);
        if (isEmpty(state.getDaySchemeName())) {
            state.setDaySchemeName(editorColorsManager.getGlobalScheme().getName());
        }
        daySchemePicker.setSelectedItem(editorColorsManager.getScheme(state.getDaySchemeName()));

        nightSchemePicker = new ComboBox(allColorSchemes);
        if (isEmpty(state.getNightSchemeName())) {
            state.setNightSchemeName(editorColorsManager.getGlobalScheme().getName());
        }
        nightSchemePicker.setSelectedItem(editorColorsManager.getScheme(state.getNightSchemeName()));

        dayTimePicker = createAndSetUpTimePicker(state.getDayStartTime());
        nightTimePicker = createAndSetUpTimePicker(state.getNightStartTime());

        changeSchNowCheckbox = new JCheckBox();
        changeSchNowCheckbox.setSelected(state.isEnabled());
        changeSchNowCheckbox.addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                setAllSettingComponentsEnabled(true);
            } else {
                setAllSettingComponentsEnabled(false);
            }
        });

    }

    public void setAllSettingComponentsEnabled(boolean b) {
        Component[] components = settingsPanel.getComponents();
        for (Component component : components) {
            component.setEnabled(b);
        }
    }

    private TimePicker createAndSetUpTimePicker(int startTimeInMinutes) {
        LocalTime dayStartTime = getLocalTimeFromMinutes(startTimeInMinutes);
        TimePickerSettings settings = new TimePickerSettings();
        settings.initialTime = dayStartTime;
        TimePicker timePicker = new TimePicker(settings);
        timePicker.getComponentToggleTimeMenuButton().setSize(5, 100);
        return timePicker;
    }

    public boolean isModified() {
        boolean changed = false;
        PluginPropertiesComponent.State state = config.getState();
        boolean enableChangeScheme = changeSchNowCheckbox.isSelected();

        if (enableChangeScheme != state.isEnabled()) {
            changed = true;
        } else {
            changed |= !getColorSchemeNameFromPicker(daySchemePicker).equalsIgnoreCase(state.getDaySchemeName());
            changed |= !getColorSchemeNameFromPicker(nightSchemePicker).equalsIgnoreCase(state.getNightSchemeName());
            changed |= (getTimeInMinutes(dayTimePicker.getTime()) != state.getDayStartTime());
            changed |= (getTimeInMinutes(nightTimePicker.getTime()) != state.getNightStartTime());
        }
        return changed;
    }

    private String getColorSchemeNameFromPicker(ComboBox daySchemePicker) {
        return ((EditorColorsScheme) daySchemePicker.getSelectedItem()).getName();
    }

    public void applyChanges() throws ConfigurationException {
        PluginPropertiesComponent.State state = config.getState();

        state.setEnabled(changeSchNowCheckbox.isSelected());
        state.setDaySchemeName(getColorSchemeNameFromPicker(daySchemePicker));
        state.setNightSchemeName(getColorSchemeNameFromPicker(nightSchemePicker));
        int dayStartTime = getTimeInMinutes(dayTimePicker.getTime());
        int nightStartTime = getTimeInMinutes(nightTimePicker.getTime());
        if (dayStartTime == nightStartTime) {
            throw new ConfigurationException("Day and Night start times cannot be the same");
        }
        state.setDayStartTime(dayStartTime);
        state.setNightStartTime(nightStartTime);

        config.loadState(state);

    }

    public void resetChanges() {
        PluginPropertiesComponent.State state = config.getState();

        changeSchNowCheckbox.setSelected(state.isEnabled());

        EditorColorsManagerImpl editorColorsManager = (EditorColorsManagerImpl) EditorColorsManager.getInstance();
        daySchemePicker.setSelectedItem(editorColorsManager.getScheme(state.getDaySchemeName()));
        nightSchemePicker.setSelectedItem(editorColorsManager.getScheme(state.getNightSchemeName()));

        dayTimePicker.setTime(getLocalTimeFromMinutes(state.getDayStartTime()));
        nightTimePicker.setTime(getLocalTimeFromMinutes(state.getNightStartTime()));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        rootPanel.add(spacer1, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        rootPanel.add(spacer2, new GridConstraints(1, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1,
                GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Choose day scheme");
        rootPanel.add(label1,
                new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Day start time");
        rootPanel.add(label2,
                new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Choose night scheme");
        rootPanel.add(label3,
                new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Night start time");
        rootPanel.add(label4,
                new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        rootPanel.add(spacer3, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1,
                GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        nightTimePicker = new TimePicker();
        rootPanel.add(nightTimePicker, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        rootPanel.add(spacer4, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        rootPanel.add(nightSchemePicker, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rootPanel.add(daySchemePicker, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dayTimePicker = new TimePicker();
        rootPanel.add(dayTimePicker, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
