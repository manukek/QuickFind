package com.quickfind.search;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ModResolver {
    private final ModPlatform modPlatform;

    public ModResolver(ModPlatform modPlatform) {
        this.modPlatform = Objects.requireNonNull(modPlatform, "modPlatform");
    }

    public List<String> suggest(String prefix) {
        String normalizedPrefix = normalize(prefix);
        return modPlatform.getLoadedModIds().stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .filter(modId -> normalize(modId).startsWith(normalizedPrefix))
                .toList();
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
