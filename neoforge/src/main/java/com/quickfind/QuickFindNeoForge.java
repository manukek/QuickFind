package com.quickfind;

import com.quickfind.ui.NeoForgeSuggestionRenderer;
import net.neoforged.fml.common.Mod;

@Mod(QuickFindCommon.MOD_ID)
public final class QuickFindNeoForge {
    public QuickFindNeoForge() {
        QuickFindCommon.init();
        try {
            Class.forName("net.minecraft.client.Minecraft");
            NeoForgeSuggestionRenderer.register();
        } catch (ClassNotFoundException ignored) {
        }
    }
}
