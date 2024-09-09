package com.daynight.plugin.forms;

import com.daynight.plugin.forms.ui.ColorSchemeComboBox;
import com.daynight.plugin.forms.ui.ThemeComboBox;
import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.state.PluginPropsState;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.daynight.plugin.utils.ColorUtils.getThemeByName;
import static com.daynight.plugin.utils.TimeUtils.getLocalTimeFromMinutes;
import static com.daynight.plugin.utils.TimeUtils.getTimeInMinutes;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DayNightConfigurableGUI {
    JPanel rootPanel;
    JPanel settingsPanel;
    ThemeComboBox dayThemePicker;
    ThemeComboBox nightThemePicker;
    TimePicker dayTimePicker;
    TimePicker nightTimePicker;
    JCheckBox enablePluginCheckbox;
    JCheckBox disableSchemePickCheckbox;
    JPanel schemePickersPanel;
    ColorSchemeComboBox daySchemePicker;
    ColorSchemeComboBox nightSchemePicker;

    PluginPropertiesStateService configService;

    private void createUIComponents() {
        EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
        LafManager lafManager = LafManager.getInstance();

        configService = PluginPropertiesStateService.getInstance();
        PluginPropsState state = configService.getState();
        fillSettingsWithDefaultsIfNeeded(state);

        Iterator<UIThemeLookAndFeelInfo> iterator = lafManager.getInstalledThemes().iterator();
        List<UIThemeLookAndFeelInfo> installedThemes = new LinkedList<>();
        iterator.forEachRemaining(installedThemes::add);

        dayThemePicker = new ThemeComboBox(installedThemes.toArray(UIThemeLookAndFeelInfo[]::new));
        dayThemePicker.setSelectedTheme(getThemeByName(state.getDayThemeName()));

        nightThemePicker = new ThemeComboBox(installedThemes.toArray(UIThemeLookAndFeelInfo[]::new));
        nightThemePicker.setSelectedTheme(getThemeByName(state.getNightThemeName()));

        daySchemePicker = new ColorSchemeComboBox(editorColorsManager.getAllSchemes());
        daySchemePicker.setSelectedScheme(editorColorsManager.getScheme(state.getDaySchemeName()));

        nightSchemePicker = new ColorSchemeComboBox(editorColorsManager.getAllSchemes());
        nightSchemePicker.setSelectedScheme(editorColorsManager.getScheme(state.getNightSchemeName()));

        dayTimePicker = createAndSetUpTimePicker(state.getDayStartTime());
        nightTimePicker = createAndSetUpTimePicker(state.getNightStartTime());

        disableSchemePickCheckbox = new JCheckBox();
        disableSchemePickCheckbox.setSelected(!state.isSchemePickEnabled());
        disableSchemePickCheckbox.addItemListener(l ->
                setSchemePickersPanelEnabled(l.getStateChange() != ItemEvent.SELECTED));

        enablePluginCheckbox = new JCheckBox();
        enablePluginCheckbox.setSelected(state.isEnabled());
        enablePluginCheckbox.addItemListener(e ->
                setAllSettingComponentsEnabled(e.getStateChange() == ItemEvent.SELECTED));
    }

    private void fillSettingsWithDefaultsIfNeeded(PluginPropsState state) {
        EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
        LafManager lafManager = LafManager.getInstance();

        UIThemeLookAndFeelInfo currentUIThemeLookAndFeel = lafManager.getCurrentUIThemeLookAndFeel();
        if (currentUIThemeLookAndFeel == null) {
            return;
        }

        if (isEmpty(state.getDayThemeName())) {
            state.setDayThemeName(currentUIThemeLookAndFeel.getName());
        }
        if (isEmpty(state.getNightThemeName())) {
            state.setNightThemeName(currentUIThemeLookAndFeel.getName());
        }
        if (isEmpty(state.getDaySchemeName())) {
            state.setDaySchemeName(editorColorsManager.getGlobalScheme().getName());
        }
        if (isEmpty(state.getNightSchemeName())) {
            state.setNightSchemeName(editorColorsManager.getGlobalScheme().getName());
        }
    }

    public void setAllSettingComponentsEnabled(boolean b) {
        disableAllPanelComponents(settingsPanel, b);
        setSchemePickersPanelEnabled(b && !disableSchemePickCheckbox.isSelected());
    }

    public void setSchemePickersPanelEnabled(boolean isEnabled) {
        disableAllPanelComponents(schemePickersPanel, isEnabled);
    }

    // TODO: 20.05.2019 Refactoring: Create component - JPanel with checkbox for enabling components inside
    private void disableAllPanelComponents(JPanel panel, boolean b) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            component.setEnabled(b);
            if (component instanceof JPanel jPanel) {
                disableAllPanelComponents(jPanel, b);
            }
        }
    }

    private TimePicker createAndSetUpTimePicker(int startTimeInMinutes) {
        LocalTime dayStartTime = getLocalTimeFromMinutes(startTimeInMinutes);
        TimePickerSettings settings = new TimePickerSettings();
        settings.initialTime = dayStartTime;
        TimePicker timePicker = new TimePicker(settings);
        timePicker.getComponentToggleTimeMenuButton().setPreferredSize(new Dimension(30, 30));
        return timePicker;
    }

    public boolean isModified() {
        boolean changed = false;
        PluginPropsState state = configService.getState();
        boolean enablePlugin = enablePluginCheckbox.isSelected();

        if (enablePlugin != state.isEnabled()) {
            changed = true;
        } else {
            changed |= !Objects.equals(state.getDayThemeName(), dayThemePicker.getSelectedThemeName());
            changed |= !Objects.equals(state.getNightThemeName(), nightThemePicker.getSelectedThemeName());

            changed |= (getTimeInMinutes(dayTimePicker.getTime()) != state.getDayStartTime());
            changed |= (getTimeInMinutes(nightTimePicker.getTime()) != state.getNightStartTime());

            changed |= state.isSchemePickEnabled() == disableSchemePickCheckbox.isSelected();

            if (!disableSchemePickCheckbox.isSelected()) {
                changed |= !Objects.equals(daySchemePicker.getSelectedSchemeName(), state.getDaySchemeName());
                changed |= !Objects.equals(nightSchemePicker.getSelectedSchemeName(), state.getNightSchemeName());
            }
        }
        return changed;
    }

    public void applyChanges() throws ConfigurationException {
        PluginPropsState state = configService.getState();

        state.setEnabled(enablePluginCheckbox.isSelected());
        state.setDayThemeName(dayThemePicker.getSelectedThemeName());
        state.setNightThemeName(nightThemePicker.getSelectedThemeName());

        int dayStartTime = getTimeInMinutes(dayTimePicker.getTime());
        int nightStartTime = getTimeInMinutes(nightTimePicker.getTime());
        if (dayStartTime == nightStartTime) {
            throw new ConfigurationException("Day and Night start times cannot be the same");
        }
        state.setDayStartTime(dayStartTime);
        state.setNightStartTime(nightStartTime);

        state.setSchemePickEnabled(!disableSchemePickCheckbox.isSelected());
        state.setDaySchemeName(daySchemePicker.getSelectedSchemeName());
        state.setNightSchemeName(nightSchemePicker.getSelectedSchemeName());

        configService.loadState(state);
    }

    public void resetChanges() {
        PluginPropsState state = configService.getState();

        enablePluginCheckbox.setSelected(state.isEnabled());
        dayThemePicker.setSelectedTheme(getThemeByName(state.getDayThemeName()));
        nightThemePicker.setSelectedTheme(getThemeByName(state.getNightThemeName()));

        dayTimePicker.setTime(getLocalTimeFromMinutes(state.getDayStartTime()));
        nightTimePicker.setTime(getLocalTimeFromMinutes(state.getNightStartTime()));

        EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();

        disableSchemePickCheckbox.setSelected(!state.isSchemePickEnabled());
        daySchemePicker.setSelectedScheme(editorColorsManager.getScheme(state.getDaySchemeName()));
        nightSchemePicker.setSelectedScheme(editorColorsManager.getScheme(state.getNightSchemeName()));
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
        rootPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 10, 10, 10), -1, -1));
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(settingsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Day theme");
        settingsPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Day start time");
        settingsPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Night theme");
        settingsPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Night start time");
        settingsPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        settingsPanel.add(dayTimePicker, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), new Dimension(160, 30), new Dimension(160, -1), 0, false));
        settingsPanel.add(nightThemePicker, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(160, -1), new Dimension(160, -1), new Dimension(160, -1), 0, false));
        settingsPanel.add(nightTimePicker, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), new Dimension(160, 30), new Dimension(160, -1), 0, false));
        final Spacer spacer1 = new Spacer();
        settingsPanel.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        settingsPanel.add(dayThemePicker, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), new Dimension(160, -1), new Dimension(160, -1), 0, false));
        schemePickersPanel = new JPanel();
        schemePickersPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        settingsPanel.add(schemePickersPanel, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Day color scheme");
        schemePickersPanel.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Night color scheme");
        schemePickersPanel.add(label6, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schemePickersPanel.add(nightSchemePicker, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(160, -1), new Dimension(160, -1), new Dimension(160, -1), 0, false));
        schemePickersPanel.add(daySchemePicker, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(160, -1), new Dimension(160, -1), new Dimension(160, -1), 0, false));
        final Spacer spacer2 = new Spacer();
        schemePickersPanel.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        disableSchemePickCheckbox.setText("Use default color scheme for selected theme");
        settingsPanel.add(disableSchemePickCheckbox, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enablePluginCheckbox.setEnabled(true);
        enablePluginCheckbox.setSelected(false);
        enablePluginCheckbox.setText("Enable automatic theme changes");
        enablePluginCheckbox.putClientProperty("hideActionText", Boolean.FALSE);
        rootPanel.add(enablePluginCheckbox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(269, 18), null, 0, false));
        final Spacer spacer3 = new Spacer();
        rootPanel.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
