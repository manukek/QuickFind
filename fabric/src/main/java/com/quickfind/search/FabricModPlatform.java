package com.quickfind.search;

import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public final class FabricModPlatform implements ModPlatform {
    @Override
    public List<String> getLoadedModIds() {
        return FabricLoader.getInstance().getAllMods().stream()
                .map(modContainer -> modContainer.getMetadata().getId())
                .toList();
    }
}
