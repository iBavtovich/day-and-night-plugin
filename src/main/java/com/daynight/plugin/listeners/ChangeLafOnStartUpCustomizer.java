package com.daynight.plugin.listeners;

import com.daynight.plugin.actions.ChangeIdeAppearanceAction;
import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.application.ApplicationManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Will be executed once action manager is initialized and ready.
 */
public class ChangeLafOnStartUpCustomizer implements AppLifecycleListener {

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        ChangeIdeAppearanceAction service = ApplicationManager.getApplication()
                .getService(ChangeIdeAppearanceAction.class);
        if (service != null) {
            service.actionPerformed(null);
        }
    }
}
