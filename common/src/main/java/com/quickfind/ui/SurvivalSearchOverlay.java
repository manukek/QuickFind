package com.quickfind.ui;

import com.quickfind.QuickFindCommon;
import com.quickfind.search.ModResolver;
import com.quickfind.search.SearchMatcher;
import com.quickfind.search.SearchQuery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.List;

public final class SurvivalSearchOverlay {
    private static final int DEFAULT_IMAGE_WIDTH = 176;
    private static final int DEFAULT_IMAGE_HEIGHT = 166;
    private static final int FIELD_HEIGHT = 14;
    private static final int FIELD_MARGIN = 4;
    public EditBox searchField;
    public boolean visible;
    private Screen screen;

    public void init(Screen screen) {
        this.screen = screen;
        this.visible = screen instanceof InventoryScreen;
        this.searchField = null;
        if (!this.visible) {
            return;
        }

        int imageWidth = getFieldValue(screen, "imageWidth", DEFAULT_IMAGE_WIDTH);
        int imageHeight = getFieldValue(screen, "imageHeight", DEFAULT_IMAGE_HEIGHT);
        int leftPos = getFieldValue(screen, "leftPos", (screen.width - imageWidth) / 2);
        int topPos = getFieldValue(screen, "topPos", (screen.height - imageHeight) / 2);
        int y = Math.max(4, topPos - FIELD_HEIGHT - FIELD_MARGIN);

        this.searchField = new EditBox(Minecraft.getInstance().font, leftPos, y, imageWidth, FIELD_HEIGHT, Component.literal("QuickFind"));
        this.searchField.setBordered(true);
        this.searchField.setMaxLength(128);
        this.searchField.setResponder(this::onQueryChanged);
        this.searchField.setVisible(true);
        this.searchField.setValue(QuickFindCommon.getLastQuery());
        this.updateLayout();
    }

    public void render(GuiGraphics guiGraphics) {
        if (!this.visible || this.searchField == null) {
            return;
        }

        this.updateLayout();

        if (this.searchField.getValue().isBlank()) {
            return;
        }

        if (!(this.screen instanceof AbstractContainerScreen<?> containerScreen)) {
            return;
        }

        String text = this.searchField.getValue();
        SearchQuery searchQuery = SearchQuery.parse(text);
        int imageWidth = getFieldValue(this.screen, "imageWidth", DEFAULT_IMAGE_WIDTH);
        int imageHeight = getFieldValue(this.screen, "imageHeight", DEFAULT_IMAGE_HEIGHT);
        int leftPos = getFieldValue(this.screen, "leftPos", (this.screen.width - imageWidth) / 2);
        int topPos = getFieldValue(this.screen, "topPos", (this.screen.height - imageHeight) / 2);

        for (Slot slot : containerScreen.getMenu().slots) {
            if (!slot.hasItem()) {
                continue;
            }

            int x = leftPos + slot.x;
            int y = topPos + slot.y;
            if (matches(slot.getItem(), text, searchQuery)) {
                AbstractContainerScreen.renderSlotHighlight(guiGraphics, x, y, 0x80FFFFFF);
            } else {
                guiGraphics.fill(x, y, x + 16, y + 16, 0xA0000000);
            }
        }
    }

    public boolean keyPressed(int key) {
        if (!this.visible || this.searchField == null || !this.searchField.isFocused()) {
            return false;
        }

        ModSuggestionWidget widget = QuickFindCommon.getModSuggestionWidget();
        if (widget != null && !widget.isVisibleOn(this.screen)) {
            QuickFindCommon.setModSuggestionWidget(null);
            widget = null;
        }

        if (widget != null && widget.keyPressed(key)) {
            return true;
        }

        return this.searchField.keyPressed(key, 0, 0) || key != 256;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ModSuggestionWidget widget = QuickFindCommon.getModSuggestionWidget();
        if (widget != null && !widget.isVisibleOn(this.screen)) {
            QuickFindCommon.setModSuggestionWidget(null);
            widget = null;
        }
        return widget != null && widget.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isVisibleOn(Screen screen) {
        return this.visible && this.searchField != null && this.screen == screen;
    }

    public void clear(Screen screen) {
        if (this.screen != screen) {
            return;
        }

        this.searchField = null;
        this.screen = null;
        this.visible = false;
        QuickFindCommon.setModSuggestionWidget(null);
    }

    public void focus(Screen screen) {
        if (!this.isVisibleOn(screen)) {
            return;
        }

        this.updateLayout();
        screen.setFocused(this.searchField);
        this.searchField.setFocused(true);
        this.searchField.moveCursorToEnd(false);
        this.updateSuggestions(this.searchField.getValue());
    }

    private void onQueryChanged(String text) {
        QuickFindCommon.setLastQuery(text);
        this.updateSuggestions(text);
    }

    private void updateSuggestions(String text) {
        if (this.searchField == null || this.screen == null) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        SearchQuery searchQuery = SearchQuery.parse(text);
        if (searchQuery.type != SearchQuery.QueryType.MOD) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        ModResolver modResolver = QuickFindCommon.getModResolver();
        if (modResolver == null) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        List<String> suggestions = modResolver.suggest(searchQuery.modId);
        if (suggestions.isEmpty()) {
            QuickFindCommon.setModSuggestionWidget(null);
            return;
        }

        int dropdownWidth = Math.max(this.searchField.getWidth(), suggestions.stream()
                .mapToInt(modId -> Minecraft.getInstance().font.width(modId) + 8)
                .max()
                .orElse(this.searchField.getWidth()));
        int dropdownX = this.searchField.getX() + this.searchField.getWidth() + 4;
        if (dropdownX + dropdownWidth > this.screen.width - 4) {
            dropdownX = Math.max(4, this.searchField.getX() - dropdownWidth - 4);
        }

        QuickFindCommon.setModSuggestionWidget(new ModSuggestionWidget(
                suggestions,
                this.screen,
                this.searchField,
                dropdownX,
                this.searchField.getY(),
                dropdownWidth,
                modId -> this.searchField.setValue("@" + modId + ":")
        ));
    }

    private void updateLayout() {
        if (this.searchField == null || this.screen == null) {
            return;
        }

        int imageWidth = getFieldValue(this.screen, "imageWidth", DEFAULT_IMAGE_WIDTH);
        int imageHeight = getFieldValue(this.screen, "imageHeight", DEFAULT_IMAGE_HEIGHT);
        int leftPos = getFieldValue(this.screen, "leftPos", (this.screen.width - imageWidth) / 2);
        int topPos = getFieldValue(this.screen, "topPos", (this.screen.height - imageHeight) / 2);
        int y = Math.max(4, topPos - FIELD_HEIGHT - FIELD_MARGIN);

        boolean changed = this.searchField.getX() != leftPos || this.searchField.getY() != y || this.searchField.getWidth() != imageWidth;
        if (!changed) {
            return;
        }

        this.searchField.setX(leftPos);
        this.searchField.setY(y);
        this.searchField.setWidth(imageWidth);
        this.updateSuggestions(this.searchField.getValue());
    }

    private static boolean matches(ItemStack stack, String text, SearchQuery searchQuery) {
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

    private static int getFieldValue(Screen screen, String fieldName, int fallback) {
        Class<?> type = screen.getClass();
        while (type != null) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.getInt(screen);
            } catch (ReflectiveOperationException ignored) {
                type = type.getSuperclass();
            }
        }
        return fallback;
    }
}
