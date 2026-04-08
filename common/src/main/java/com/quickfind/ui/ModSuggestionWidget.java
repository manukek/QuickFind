package com.quickfind.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class ModSuggestionWidget {
    private static final int PADDING = 2;
    private static final int ROW_HEIGHT = 12;
    public final List<String> suggestions;
    public int selectedIndex;
    public int x;
    public int y;
    public int width;
    private final Screen owner;
    private final EditBox anchor;
    private final Consumer<String> onSelect;

    public ModSuggestionWidget(List<String> suggestions, Screen owner, EditBox anchor, int x, int y, int width, Consumer<String> onSelect) {
        this.suggestions = new ArrayList<>(suggestions);
        this.selectedIndex = this.suggestions.isEmpty() ? -1 : 0;
        this.x = x;
        this.y = y;
        this.width = width;
        this.owner = Objects.requireNonNull(owner, "owner");
        this.anchor = Objects.requireNonNull(anchor, "anchor");
        this.onSelect = Objects.requireNonNull(onSelect, "onSelect");
    }

    public boolean isVisibleOn(Screen screen) {
        return !this.suggestions.isEmpty() && this.owner == screen && this.anchor.isVisible() && this.anchor.isFocused();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.suggestions.isEmpty()) {
            return;
        }

        guiGraphics.nextStratum();

        int height = this.getHeight();
        guiGraphics.fill(this.x, this.y, this.x + this.width, this.y + height, 0xE0101010);

        int hoveredIndex = this.getSuggestionIndex(mouseX, mouseY);
        for (int i = 0; i < this.suggestions.size(); i++) {
            int rowTop = this.y + PADDING + i * ROW_HEIGHT;
            if (i == this.selectedIndex || i == hoveredIndex) {
                guiGraphics.fill(this.x + 1, rowTop, this.x + this.width - 1, rowTop + ROW_HEIGHT, 0x80406080);
            }
            guiGraphics.drawString(Minecraft.getInstance().font, this.suggestions.get(i), this.x + 4, rowTop + 2, 0xFFFFFFFF);
        }
    }

    public boolean keyPressed(int key) {
        if (this.suggestions.isEmpty()) {
            return false;
        }

        if (key == 265) {
            this.selectedIndex = Math.max(this.selectedIndex - 1, 0);
            return true;
        }

        if (key == 264) {
            this.selectedIndex = Math.min(this.selectedIndex + 1, this.suggestions.size() - 1);
            return true;
        }

        if (key == 257 || key == 258 || key == 335) {
            this.select(this.selectedIndex);
            return true;
        }

        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }

        int index = this.getSuggestionIndex(mouseX, mouseY);
        if (index < 0) {
            return false;
        }

        this.selectedIndex = index;
        this.select(index);
        return true;
    }

    private int getSuggestionIndex(double mouseX, double mouseY) {
        if (mouseX < this.x || mouseX > this.x + this.width) {
            return -1;
        }

        double innerY = mouseY - this.y - PADDING;
        if (innerY < 0) {
            return -1;
        }

        int index = (int) (innerY / ROW_HEIGHT);
        if (index < 0 || index >= this.suggestions.size()) {
            return -1;
        }

        return index;
    }

    private int getHeight() {
        return PADDING * 2 + this.suggestions.size() * ROW_HEIGHT;
    }

    private void select(int index) {
        if (index < 0 || index >= this.suggestions.size()) {
            return;
        }
        this.onSelect.accept(this.suggestions.get(index));
    }
}
