package com.quickfind;

import com.quickfind.ui.FabricSuggestionRenderer;
import net.fabricmc.api.ClientModInitializer;

public final class QuickFindFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        QuickFindCommon.init();
        FabricSuggestionRenderer.register();
    }
}
