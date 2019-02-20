package com.daynight.plugin.utils;

import com.daynight.plugin.components.PluginPropertiesComponent;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class TimeUtils {

    public static final int SEC_IN_DAY = 24 * 60 * 60;

    public static boolean isDayNow(PluginPropertiesComponent.State state) {
        int timeInMinutes = getTimeInMinutes(LocalTime.now());
        if (state.getNightStartTime() < state.getDayStartTime()) {
            return !(timeInMinutes >= state.getNightStartTime() && timeInMinutes < state.getDayStartTime());
        } else {
            return timeInMinutes >= state.getDayStartTime() && timeInMinutes < state.getNightStartTime();
        }
    }

    public static boolean isNightNow(PluginPropertiesComponent.State state) {
        return !isDayNow(state);
    }

    public static int timeFromNowToEventInSec(LocalTime eventTime) {
         LocalTime now = LocalTime.now();
        if (now.isBefore(eventTime)) {
            return eventTime.toSecondOfDay() - now.toSecondOfDay();
        } else {
            return SEC_IN_DAY + eventTime.toSecondOfDay() - now.toSecondOfDay();
        }
    }

    public static int getTimeInMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    public static LocalTime getLocalTimeFromMinutes(int minOfDay) {
        int minutes = minOfDay % 60;
        int hours = (minOfDay - minutes) / 60;
        return LocalTime.of(hours, minutes);
    }
}
