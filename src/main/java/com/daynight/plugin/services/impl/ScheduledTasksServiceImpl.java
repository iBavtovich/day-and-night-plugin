package com.daynight.plugin.services.impl;

import static com.daynight.plugin.utils.TimeUtils.SEC_IN_DAY;
import static com.daynight.plugin.utils.TimeUtils.getLocalTimeFromMinutes;
import static com.daynight.plugin.utils.TimeUtils.timeFromNowToEventInSec;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.daynight.plugin.services.PluginPropertiesStateService;
import com.daynight.plugin.services.ScheduledTasksService;
import com.daynight.plugin.state.PluginPropsState;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.concurrency.EdtExecutorService;

import java.awt.event.InputEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ScheduledTasksServiceImpl implements ScheduledTasksService {

    ScheduledFuture<?> switchToDayScheme;
    ScheduledFuture<?> switchToNightScheme;

    private final Runnable actionForSchemeSwitching = () -> {
        ActionManager actionManager = ActionManager.getInstance();
        AnAction action = actionManager.getAction("DayNightChangeColor");
        action.actionPerformed(null);
    };

    @Override
    public void submitTasksIfNeeded() {
        ScheduledExecutorService service = AppExecutorUtil.getAppScheduledExecutorService();
        PluginPropsState state = PluginPropertiesStateService.getInstance().getState();
        if (state.isEnabled()) {
            switchToDayScheme = service.scheduleWithFixedDelay(actionForSchemeSwitching,
                    timeFromNowToEventInSec(getLocalTimeFromMinutes(state.getDayStartTime())), SEC_IN_DAY, SECONDS);

            switchToNightScheme = service.scheduleWithFixedDelay(actionForSchemeSwitching,
                    timeFromNowToEventInSec(getLocalTimeFromMinutes(state.getNightStartTime())), SEC_IN_DAY, SECONDS);
        }
    }

    @Override
    public void cancelTasksIfExists() {
        if (switchToDayScheme != null) {
            switchToDayScheme.cancel(true);
        }
        if (switchToNightScheme != null) {
            switchToNightScheme.cancel(true);
        }
    }
}
