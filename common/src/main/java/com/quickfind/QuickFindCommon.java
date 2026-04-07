package com.quickfind;

import com.quickfind.ui.ModSuggestionWidget;
import com.quickfind.ui.SurvivalSearchOverlay;

public final class QuickFindCommon {
    public static final String MOD_ID = "quickfind";
    private static ModSuggestionWidget modSuggestionWidget;
    private static final SurvivalSearchOverlay survivalSearchOverlay = new SurvivalSearchOverlay();
    private static String lastQuery = "";

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

    public static SurvivalSearchOverlay getSurvivalSearchOverlay() {
        return survivalSearchOverlay;
    }

    public static String getLastQuery() {
        return lastQuery;
    }

    public static void setLastQuery(String lastQuery) {
        QuickFindCommon.lastQuery = lastQuery == null ? "" : lastQuery;
    }
}
