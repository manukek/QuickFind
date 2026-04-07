package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public final class NeoForgeSurvivalOverlay {
    private NeoForgeSurvivalOverlay() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(NeoForgeSurvivalOverlay::onInitPost);
        MinecraftForge.EVENT_BUS.addListener(NeoForgeSurvivalOverlay::onRenderPost);
        MinecraftForge.EVENT_BUS.addListener(NeoForgeSurvivalOverlay::onClosing);
    }

    private static void onInitPost(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen)) {
            return;
        }

        SurvivalSearchOverlay overlay = QuickFindCommon.getSurvivalSearchOverlay();
        overlay.init(event.getScreen());
        if (overlay.searchField != null) {
            event.addListener(overlay.searchField);
        }
    }

    private static void onRenderPost(ScreenEvent.Render.Post event) {
        SurvivalSearchOverlay overlay = QuickFindCommon.getSurvivalSearchOverlay();
        if (overlay.isVisibleOn(event.getScreen())) {
            overlay.render(event.getGuiGraphics());
        }
    }

    private static void onClosing(ScreenEvent.Closing event) {
        QuickFindCommon.getSurvivalSearchOverlay().clear(event.getScreen());
    }
}
