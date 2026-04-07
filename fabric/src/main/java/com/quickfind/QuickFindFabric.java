package com.quickfind;

import com.quickfind.ui.FabricKeyBindings;
import com.quickfind.ui.FabricSuggestionRenderer;
import com.quickfind.ui.FabricSurvivalOverlay;
import net.fabricmc.api.ClientModInitializer;

public final class QuickFindFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        QuickFindCommon.init();
        FabricKeyBindings.register();
        FabricSuggestionRenderer.register();
        FabricSurvivalOverlay.register();
    }
}
