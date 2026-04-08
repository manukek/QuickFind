package com.quickfind;

import com.quickfind.search.NeoForgeModPlatform;
import com.quickfind.ui.NeoForgeKeyBindings;
import com.quickfind.ui.NeoForgeSuggestionRenderer;
import com.quickfind.ui.NeoForgeSurvivalOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(QuickFindCommon.MOD_ID)
public final class QuickFindNeoForge {
    public QuickFindNeoForge() {
        QuickFindCommon.init(new NeoForgeModPlatform());
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        try {
            Class.forName("net.minecraft.client.Minecraft");
            NeoForgeKeyBindings.register(modEventBus);
            NeoForgeSuggestionRenderer.register();
            NeoForgeSurvivalOverlay.register();
        } catch (ClassNotFoundException ignored) {
        }
    }
}
