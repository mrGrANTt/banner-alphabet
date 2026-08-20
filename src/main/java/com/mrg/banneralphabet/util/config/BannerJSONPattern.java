package com.mrg.banneralphabet.util.config;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class BannerJSONPattern {
    private final String pattern;
    private final int color;

    public BannerJSONPattern(BannerPatternsComponent.Layer layer, DyeColor back) {
        pattern = layer.pattern().getIdAsString();
        color = layer.color() == back ? 0 : 1;
    }
    public BannerJSONPattern() {
        pattern = "base";
        color = 0;
    }

    public int getColor() { return color; }
    public RegistryEntry<BannerPattern> getPattern(RegistryEntryLookup<BannerPattern> lookup) {
        return lookup.getOrThrow(RegistryKey.of(RegistryKeys.BANNER_PATTERN, Identifier.of(pattern)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            return this.hashCode() == obj.hashCode();
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern + ":" + color);
    }
}
