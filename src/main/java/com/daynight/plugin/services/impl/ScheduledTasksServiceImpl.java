package com.daynight.plugin.services.impl;

import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.ScheduledTasksService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.concurrency.AppExecutorUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.daynight.plugin.utils.TimeUtils.getLocalTimeFromMinutes;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ScheduledTasksServiceImpl implements ScheduledTasksService {
    private ScheduledFuture<?> switchingFuture;

    @Override
    public void submitTasksIfNeeded() {
        ScheduledExecutorService service = AppExecutorUtil.getAppScheduledExecutorService();
        PluginPropsState state = PluginPropertiesStateService.getInstance().getState();
        if (state.isEnabled()) {
            LocalTime dayStartTime = getLocalTimeFromMinutes(state.getDayStartTime());
            LocalTime nightStartTime = getLocalTimeFromMinutes(state.getNightStartTime());
            SwitchIdeAppearanceTask dayStartTask = SwitchIdeAppearanceTask.withCloserDayAtTime(dayStartTime);
            SwitchIdeAppearanceTask nightStartTask = SwitchIdeAppearanceTask.withCloserDayAtTime(nightStartTime);
            List<SwitchIdeAppearanceTask> tasksToCheck = Arrays.asList(dayStartTask, nightStartTask);

            switchingFuture = service.scheduleWithFixedDelay(() -> {
                for (SwitchIdeAppearanceTask task : tasksToCheck) {
                    if (task.isTimeToSwitch()) {
                        changeColorScheme();
                        task.extendForOneDay();
                    }
                }
            }, 0, 1, SECONDS);
        }
    }

    @Override
    public void cancelTask() {
        if (switchingFuture != null) {
            switchingFuture.cancel(true);
        }
    }

    private void changeColorScheme() {
        AnAction action = ActionManager.getInstance().getAction("DayNightChangeColor");
        DataManager manager = DataManager.getInstance();
        if (manager != null) {
            manager.getDataContextFromFocusAsync().onSuccess(dataContext ->
                action.actionPerformed(AnActionEvent.createFromAnAction(action, null, "", dataContext))
            );
        }
    }

    @Override
    public void dispose() {
        cancelTask();
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private static class SwitchIdeAppearanceTask {
        private LocalDateTime executionDateTime;

        public void extendForOneDay() {
            executionDateTime = executionDateTime.plusDays(1L);
        }

        public boolean isTimeToSwitch() {
            LocalDateTime now = LocalDateTime.now();
            return now.isEqual(executionDateTime) || now.isAfter(executionDateTime);
        }

        public static SwitchIdeAppearanceTask withCloserDayAtTime(LocalTime executionTime) {
            SwitchIdeAppearanceTask result = new SwitchIdeAppearanceTask();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime timeToday = LocalDateTime.of(LocalDate.now(), executionTime);
            if (now.isEqual(timeToday) || now.isAfter(timeToday)) /* Time already passed -> submit for tomorrow */ {
                result.setExecutionDateTime(timeToday.plusDays(1L));
            } else {
                result.setExecutionDateTime(timeToday);
            }
            return result;
        }
    }
}
