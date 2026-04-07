package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public final class FabricSurvivalOverlay {
    private FabricSurvivalOverlay() {
    }

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof InventoryScreen)) {
                return;
            }

            SurvivalSearchOverlay overlay = QuickFindCommon.getSurvivalSearchOverlay();
            overlay.init(screen);
            if (overlay.searchField != null) {
                Screens.getButtons(screen).add(overlay.searchField);
            }

            ScreenEvents.afterRender(screen).register((currentScreen, guiGraphics, mouseX, mouseY, tickDelta) -> {
                if (overlay.isVisibleOn(currentScreen)) {
                    overlay.render(guiGraphics);
                }
            });
            ScreenEvents.remove(screen).register(overlay::clear);
        });
    }
}
