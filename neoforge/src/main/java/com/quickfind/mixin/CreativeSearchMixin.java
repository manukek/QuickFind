package com.quickfind.mixin;

import com.quickfind.QuickFindCommon;
import com.quickfind.search.ModResolver;
import com.quickfind.search.NeoForgeModPlatform;
import com.quickfind.search.SearchMatcher;
import com.quickfind.search.SearchQuery;
import com.quickfind.ui.ModSuggestionWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeSearchMixin {
    private static final ModResolver QUICKFIND_MOD_RESOLVER = new ModResolver(new NeoForgeModPlatform());

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
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        String text = this.searchBox.getValue();
        QuickFindCommon.setLastQuery(text);
        SearchQuery searchQuery = SearchQuery.parse(text);
        this.quickfind$updateSuggestions(searchQuery);
        if (text.trim().isEmpty() || text.startsWith("#")) {
            return;
        }

        CreativeModeInventoryScreen.ItemPickerMenu menu = (CreativeModeInventoryScreen.ItemPickerMenu) ((AbstractContainerScreenAccessor) this).quickfind$getMenu();
        menu.items.clear();
        this.visibleTags.clear();

        for (ItemStack stack : selectedTab.getDisplayItems()) {
            if (this.quickfind$matches(stack, text, searchQuery)) {
                menu.items.add(stack);
            }
        }

        this.scrollOffs = 0.0F;
        menu.scrollTo(0.0F);
        ci.cancel();
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void quickfind$init(CallbackInfo ci) {
        this.quickfind$restoreLastQuery();
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void quickfind$selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {
        this.quickfind$restoreLastQuery();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void quickfind$keyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        ModSuggestionWidget widget = QuickFindCommon.getModSuggestionWidget();
        if (widget != null && !widget.isVisibleOn((Screen) (Object) this)) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        if (widget != null && widget.keyPressed(event.key())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void quickfind$mouseClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        ModSuggestionWidget widget = QuickFindCommon.getModSuggestionWidget();
        if (widget != null && !widget.isVisibleOn((Screen) (Object) this)) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        if (widget != null && widget.mouseClicked(event.x(), event.y(), event.button())) {
            cir.setReturnValue(true);
        }
    }

    private boolean quickfind$matches(ItemStack stack, String text, SearchQuery searchQuery) {
        if (stack.isEmpty()) {
            return false;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String namespace = itemId == null ? "" : itemId.getNamespace();
        String itemName = stack.getHoverName().getString();

        if (searchQuery.type == SearchQuery.QueryType.MOD) {
            return namespace.startsWith(searchQuery.modId);
        }

        if (searchQuery.type == SearchQuery.QueryType.MOD_ITEM) {
            boolean namespaceMatches = searchQuery.modId == null || searchQuery.modId.isEmpty() || namespace.equals(searchQuery.modId);
            boolean itemMatches = searchQuery.itemQuery == null || searchQuery.itemQuery.isEmpty() || SearchMatcher.matchesAny(searchQuery.itemQuery, itemName);
            return namespaceMatches && itemMatches;
        }

        return SearchMatcher.matchesAny(text, itemName);
    }

    private void quickfind$updateSuggestions(SearchQuery searchQuery) {
        if (searchQuery.type != SearchQuery.QueryType.MOD) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        List<String> suggestions = QUICKFIND_MOD_RESOLVER.suggest(searchQuery.modId);
        if (suggestions.isEmpty()) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        Screen screen = (Screen) (Object) this;
        int dropdownWidth = Math.max(this.searchBox.getWidth(), suggestions.stream()
                .mapToInt(modId -> Minecraft.getInstance().font.width(modId) + 8)
                .max()
                .orElse(this.searchBox.getWidth()));
        int dropdownX = this.searchBox.getX() + this.searchBox.getWidth() + 4;
        if (dropdownX + dropdownWidth > screen.width - 4) {
            dropdownX = Math.max(4, this.searchBox.getX() - dropdownWidth - 4);
        }

        QuickFindCommon.setModSuggestionWidget(new ModSuggestionWidget(
                suggestions,
                screen,
                this.searchBox,
                dropdownX,
                this.searchBox.getY(),
                dropdownWidth,
                modId -> this.searchBox.setValue("@" + modId + ":")
        ));
    }

    private void quickfind$restoreLastQuery() {
        if (!selectedTab.hasSearchBar() || this.searchBox == null || !this.searchBox.getValue().isEmpty()) {
            return;
        }

        String lastQuery = QuickFindCommon.getLastQuery();
        if (!lastQuery.isEmpty()) {
            this.searchBox.setValue(lastQuery);
        }
    }
}
