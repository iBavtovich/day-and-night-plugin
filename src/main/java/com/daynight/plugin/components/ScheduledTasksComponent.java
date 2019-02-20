package com.daynight.plugin.components;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.intellij.util.concurrency.EdtExecutorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.awt.event.InputEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static com.daynight.plugin.utils.TimeUtils.SEC_IN_DAY;
import static com.daynight.plugin.utils.TimeUtils.getLocalTimeFromMinutes;
import static com.daynight.plugin.utils.TimeUtils.timeFromNowToEventInSec;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduledTasksComponent {

    final PluginPropertiesComponent properties;

    ScheduledFuture<?> switchToDayScheme;
    ScheduledFuture<?> switchToNightScheme;

    private Runnable actionForSchemeSwitching = () -> {
        AnAction action = ActionManager.getInstance().getAction("DayNightChangeColor");
        InputEvent inputEvent = ActionCommand.getInputEvent("RightClickButton");

        ActionManager.getInstance().tryToExecute(action, inputEvent, null, null, true);
    };

    public void submitTasksIfNeeded() {
        ScheduledExecutorService service = EdtExecutorService.getScheduledExecutorInstance();
        PluginPropertiesComponent.State state = properties.getState();

        if (state.isEnabled()) {
            switchToDayScheme = service.scheduleWithFixedDelay(actionForSchemeSwitching,
                    timeFromNowToEventInSec(getLocalTimeFromMinutes(state.getDayStartTime())), SEC_IN_DAY, SECONDS);

            switchToNightScheme = service.scheduleWithFixedDelay(actionForSchemeSwitching,
                    timeFromNowToEventInSec(getLocalTimeFromMinutes(state.getNightStartTime())), SEC_IN_DAY, SECONDS);
        }
    }

    public void cancelTasksIfExists() {
        if (switchToDayScheme != null) {
            switchToDayScheme.cancel(true);
        }
        if (switchToNightScheme != null) {
            switchToNightScheme.cancel(true);
        }
    }

}
