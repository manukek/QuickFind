package com.quickfind.mixin;

import com.quickfind.QuickFindCommon;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class InventorySearchMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void quickfind$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (QuickFindCommon.getSurvivalSearchOverlay().isVisibleOn((Screen) (Object) this)
                && QuickFindCommon.getSurvivalSearchOverlay().keyPressed(keyCode)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void quickfind$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (QuickFindCommon.getSurvivalSearchOverlay().isVisibleOn((Screen) (Object) this)
                && QuickFindCommon.getSurvivalSearchOverlay().mouseClicked(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }
}
