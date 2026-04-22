package com.quickfind.mixin;

import com.quickfind.ui.QuickFindContainerScreenAccess;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor extends QuickFindContainerScreenAccess {
    @Accessor("menu")
    AbstractContainerMenu quickfind$getMenu();

    @Accessor("imageWidth")
    int quickfind$getImageWidth();

    @Accessor("imageHeight")
    int quickfind$getImageHeight();

    @Accessor("leftPos")
    int quickfind$getLeftPos();

    @Accessor("topPos")
    int quickfind$getTopPos();
}
