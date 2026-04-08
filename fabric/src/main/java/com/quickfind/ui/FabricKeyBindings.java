package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class FabricKeyBindings {
    private static final KeyMapping FOCUS_SEARCH = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.quickfind.focus_search", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, KeyMapping.Category.INVENTORY)
    );

    private FabricKeyBindings() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(FabricKeyBindings::onEndClientTick);
    }

    private static void onEndClientTick(Minecraft minecraft) {
        while (FOCUS_SEARCH.consumeClick()) {
            if (minecraft.screen == null) {
                continue;
            }

            QuickFindCommon.getSurvivalSearchOverlay().focus(minecraft.screen);
        }
    }
}
