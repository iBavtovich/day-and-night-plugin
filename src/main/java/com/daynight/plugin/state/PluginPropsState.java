package com.daynight.plugin.state;

import com.daynight.plugin.utils.TimeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PluginPropsState {

    boolean isEnabled;
    boolean schemePickEnabled;
    // Time in both cases is the number of minutes from midnight (e.g. 1 -> 0:01AM, 60 -> 1:00AM and so on)
    int dayStartTime;
    int nightStartTime;

    String daySchemeName;
    String nightSchemeName;

    String dayThemeName;
    String nightThemeName;

    @Nullable
    public String getThemeNameForCurrentTime() {
        return TimeUtils.isDayNow(this) ? dayThemeName : nightThemeName;
    }

    @Nullable
    public String getColorSchemeNameForCurrentTime() {
        if (!schemePickEnabled) {
            return null;
        }
        return TimeUtils.isDayNow(this) ? daySchemeName : nightSchemeName;
    }
}
