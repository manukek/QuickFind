package com.quickfind.mixin;

import com.quickfind.search.SearchMatcher;
import com.quickfind.search.SearchQuery;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeSearchMixin {
    @Shadow
    @Final
    private static CreativeModeTab selectedTab;

    @Shadow
    private EditBox searchBox;

    @Shadow
    @Final
    private Set<TagKey<Item>> visibleTags;

    @Shadow
    private float scrollOffs;

    @Inject(method = "refreshSearchResults", at = @At("HEAD"), cancellable = true)
    private void quickfind$refreshSearchResults(CallbackInfo ci) {
        if (!selectedTab.hasSearchBar()) {
            return;
        }

        String text = this.searchBox.getValue();
        SearchQuery searchQuery = SearchQuery.parse(text);
        if (text.trim().isEmpty() || text.startsWith("#") || searchQuery.type == SearchQuery.QueryType.MOD || searchQuery.type == SearchQuery.QueryType.MOD_ITEM) {
            return;
        }

        CreativeModeInventoryScreen.ItemPickerMenu menu = (CreativeModeInventoryScreen.ItemPickerMenu) ((AbstractContainerScreenAccessor) this).quickfind$getMenu();
        menu.items.clear();
        this.visibleTags.clear();

        for (ItemStack stack : selectedTab.getDisplayItems()) {
            if (!stack.isEmpty() && SearchMatcher.matchesAny(text, stack.getHoverName().getString())) {
                menu.items.add(stack);
            }
        }

        this.scrollOffs = 0.0F;
        menu.scrollTo(0.0F);
        ci.cancel();
    }
}
