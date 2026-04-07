package com.quickfind;

import net.fabricmc.api.ClientModInitializer;

public final class QuickFindFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        QuickFindCommon.init();
    }
}
