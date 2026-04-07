package com.quickfind.search;

import net.neoforged.fml.ModList;

import java.util.List;

public final class NeoForgeModPlatform implements ModPlatform {
    @Override
    public List<String> getLoadedModIds() {
        return ModList.get().getMods().stream()
                .map(modInfo -> modInfo.getModId())
                .toList();
    }
}
