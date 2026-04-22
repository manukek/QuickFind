package com.quickfind.ui;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface QuickFindContainerScreenAccess {
    AbstractContainerMenu quickfind$getMenu();

    int quickfind$getImageWidth();

    int quickfind$getImageHeight();

    int quickfind$getLeftPos();

    int quickfind$getTopPos();
}
