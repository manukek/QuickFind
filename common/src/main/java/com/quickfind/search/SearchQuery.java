package com.quickfind.search;

import java.util.Locale;

public final class SearchQuery {
    public final QueryType type;
    public final String modId;
    public final String itemQuery;

    public SearchQuery(QueryType type, String modId, String itemQuery) {
        this.type = type;
        this.modId = modId;
        this.itemQuery = itemQuery;
    }

    public static SearchQuery parse(String input) {
        String trimmedInput = input == null ? "" : input.trim();
        if (trimmedInput.startsWith("@")) {
            return parseModQuery(trimmedInput.substring(1));
        }

        String normalizedInput = normalizeWhitespace(trimmedInput);
        if (normalizedInput.isEmpty()) {
            return new SearchQuery(QueryType.TEXT, null, null);
        }

        if (normalizedInput.indexOf(' ') >= 0) {
            return new SearchQuery(QueryType.FUZZY, null, null);
        }

        return new SearchQuery(QueryType.TEXT, null, null);
    }

    private static SearchQuery parseModQuery(String input) {
        int separatorIndex = input.indexOf(':');
        if (separatorIndex < 0) {
            return new SearchQuery(QueryType.MOD, normalizeModId(input), null);
        }

        String modId = normalizeModId(input.substring(0, separatorIndex));
        String itemQuery = normalizeWhitespace(input.substring(separatorIndex + 1));
        return new SearchQuery(QueryType.MOD_ITEM, modId, itemQuery);
    }

    private static String normalizeModId(String input) {
        return normalizeWhitespace(input).toLowerCase(Locale.ROOT);
    }

    private static String normalizeWhitespace(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    public enum QueryType {
        TEXT,
        FUZZY,
        MOD,
        MOD_ITEM
    }
}
