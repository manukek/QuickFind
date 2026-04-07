package com.quickfind.search;

import java.util.Locale;

public final class SearchMatcher {
    private SearchMatcher() {
    }

    public static boolean matchesInitials(String query, String itemName) {
        String[] queryWords = splitWords(query);
        if (queryWords.length == 0) {
            return true;
        }

        String[] itemWords = splitWords(itemName);
        if (itemWords.length == 0) {
            return false;
        }

        int itemIndex = 0;
        for (String queryWord : queryWords) {
            boolean matched = false;
            while (itemIndex < itemWords.length) {
                if (itemWords[itemIndex].startsWith(queryWord)) {
                    matched = true;
                    itemIndex++;
                    break;
                }
                itemIndex++;
            }
            if (!matched) {
                return false;
            }
        }

        return true;
    }

    public static boolean matchesFull(String query, String itemName) {
        String normalizedQuery = normalize(query);
        if (normalizedQuery.isEmpty()) {
            return true;
        }

        String normalizedItemName = normalize(itemName);
        if (normalizedItemName.isEmpty()) {
            return false;
        }

        return normalizedItemName.contains(normalizedQuery);
    }

    public static boolean matchesAny(String query, String itemName) {
        return matchesFull(query, itemName) || matchesInitials(query, itemName);
    }

    private static String[] splitWords(String value) {
        String normalized = normalize(value);
        if (normalized.isEmpty()) {
            return new String[0];
        }
        return normalized.split(" ");
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{Nd}]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }
}
