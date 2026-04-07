package com.quickfind.search;

import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class ModResolver {
    private final ModPlatform modPlatform;

    public ModResolver(ModPlatform modPlatform) {
        this.modPlatform = Objects.requireNonNull(modPlatform, "modPlatform");
    }

    public List<String> suggest(String prefix) {
        String normalizedPrefix = normalize(prefix);
        Set<String> itemNamespaces = BuiltInRegistries.ITEM.keySet().stream()
                .map(resourceLocation -> normalize(resourceLocation.getNamespace()))
                .collect(Collectors.toSet());

        return modPlatform.getLoadedModIds().stream()
                .filter(Objects::nonNull)
                .filter(modId -> itemNamespaces.contains(normalize(modId)))
                .distinct()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .filter(modId -> normalize(modId).startsWith(normalizedPrefix))
                .limit(8)
                .toList();
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
