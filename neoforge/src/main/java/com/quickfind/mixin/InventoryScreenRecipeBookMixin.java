package com.quickfind.mixin;

import com.quickfind.QuickFindCommon;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenRecipeBookMixin {
    @Inject(method = "onRecipeBookButtonClick", at = @At("TAIL"))
    private void quickfind$refreshOverlayLayout(CallbackInfo ci) {
        QuickFindCommon.getSurvivalSearchOverlay().refreshLayout((InventoryScreen) (Object) this);
    }
}
