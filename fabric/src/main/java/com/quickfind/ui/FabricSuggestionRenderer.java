package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public final class FabricSuggestionRenderer {
    private FabricSuggestionRenderer() {
    }

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                ScreenEvents.afterRender(screen).register((currentScreen, guiGraphics, mouseX, mouseY, tickDelta) -> {
                    ModSuggestionWidget widget = QuickFindCommon.getModSuggestionWidget();
                    if (widget != null) {
                        widget.render(guiGraphics, mouseX, mouseY);
                    }
                }));
    }
}
