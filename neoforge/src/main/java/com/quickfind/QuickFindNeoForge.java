package com.quickfind;

import com.quickfind.ui.NeoForgeKeyBindings;
import com.quickfind.ui.NeoForgeSuggestionRenderer;
import com.quickfind.ui.NeoForgeSurvivalOverlay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(QuickFindCommon.MOD_ID)
public final class QuickFindNeoForge {
    public QuickFindNeoForge(IEventBus modEventBus) {
        QuickFindCommon.init();
        try {
            Class.forName("net.minecraft.client.Minecraft");
            NeoForgeKeyBindings.register(modEventBus);
            NeoForgeSuggestionRenderer.register();
            NeoForgeSurvivalOverlay.register();
        } catch (ClassNotFoundException ignored) {
        }
    }
}
