package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class NeoForgeSuggestionRenderer {
    private NeoForgeSuggestionRenderer() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(ScreenEvent.Render.Post.class, NeoForgeSuggestionRenderer::onRenderPost);
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
