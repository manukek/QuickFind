package com.quickfind.mixin;

import com.quickfind.QuickFindCommon;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class InventorySearchMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void quickfind$keyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (QuickFindCommon.getSurvivalSearchOverlay().isVisibleOn((Screen) (Object) this)
                && QuickFindCommon.getSurvivalSearchOverlay().keyPressed(event.key())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void quickfind$mouseClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (QuickFindCommon.getSurvivalSearchOverlay().isVisibleOn((Screen) (Object) this)
                && QuickFindCommon.getSurvivalSearchOverlay().mouseClicked(event.x(), event.y(), event.button())) {
            cir.setReturnValue(true);
        }
    }
}
