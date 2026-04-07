package com.quickfind.ui;

import com.mojang.blaze3d.platform.InputConstants;
import com.quickfind.QuickFindCommon;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

public final class NeoForgeKeyBindings {
    private static final KeyMapping FOCUS_SEARCH = new KeyMapping("key.quickfind.focus_search", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, KeyMapping.CATEGORY_INVENTORY);

    private NeoForgeKeyBindings() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(NeoForgeKeyBindings::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, NeoForgeKeyBindings::onClientTick);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(FOCUS_SEARCH);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        while (FOCUS_SEARCH.consumeClick()) {
            if (minecraft.screen == null) {
                continue;
            }

            QuickFindCommon.getSurvivalSearchOverlay().focus(minecraft.screen);
        }
    }
}
