package com.quickfind;

import com.quickfind.search.FabricModPlatform;
import com.quickfind.ui.FabricKeyBindings;
import com.quickfind.ui.FabricSuggestionRenderer;
import com.quickfind.ui.FabricSurvivalOverlay;
import net.fabricmc.api.ClientModInitializer;

public final class QuickFindFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        QuickFindCommon.init(new FabricModPlatform());
        FabricKeyBindings.register();
        FabricSuggestionRenderer.register();
        FabricSurvivalOverlay.register();
    }
}
