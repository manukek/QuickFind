package com.quickfind;

import com.quickfind.ui.ModSuggestionWidget;

public final class QuickFindCommon {
    public static final String MOD_ID = "quickfind";
    private static ModSuggestionWidget modSuggestionWidget;

    private QuickFindCommon() {
    }

    public static void init() {
    }

    public static ModSuggestionWidget getModSuggestionWidget() {
        return modSuggestionWidget;
    }

    public static void setModSuggestionWidget(ModSuggestionWidget modSuggestionWidget) {
        QuickFindCommon.modSuggestionWidget = modSuggestionWidget;
    }
}
