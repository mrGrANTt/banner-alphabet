package com.mrg.banneralphabet.util.config;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;

import java.util.Objects;

public class BannerJSONPattern {
    private final String pattern;
    private final int color;

    public BannerJSONPattern(BannerPatternLayers.Layer layer, DyeColor back) {
        pattern = layer.pattern().getRegisteredName();
        color = layer.color() == back ? 0 : 1;
    }
    public BannerJSONPattern() {
        pattern = "base";
        color = 0;
    }

    public int getColor() { return color; }
    public Holder<BannerPattern> getPattern(HolderGetter<BannerPattern> lookup) {
        return lookup.getOrThrow(ResourceKey.create(Registries.BANNER_PATTERN, Identifier.parse(pattern)));
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
