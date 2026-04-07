package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public final class NeoForgeSuggestionRenderer {
    private NeoForgeSuggestionRenderer() {
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(NeoForgeSuggestionRenderer::onRenderPost);
    }

    private static void onRenderPost(ScreenEvent.Render.Post event) {
        ModSuggestionWidget widget = QuickFindCommon.getModSuggestionWidget();
        if (widget == null) {
            return;
        }

        if (!widget.isVisibleOn(event.getScreen())) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        widget.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY());
    }
}
