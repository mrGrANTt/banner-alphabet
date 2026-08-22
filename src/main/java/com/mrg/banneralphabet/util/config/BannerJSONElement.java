package com.mrg.banneralphabet.util.config;

import net.minecraft.block.BannerBlock;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BannerJSONElement {
    public List<BannerJSONPattern> patterns;

    public BannerJSONElement() {
        patterns = List.of();
    }
    public BannerJSONElement(ItemStack banner) {
        BannerPatternsComponent component = banner.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
        List<BannerPatternsComponent.Layer> layers = new ArrayList<>(component.layers());
        BannerItem item = (BannerItem) banner.getItem();

        patterns = new ArrayList<>();
        for (BannerPatternsComponent.Layer layer : layers)
            patterns.add(new BannerJSONPattern(layer, item.getColor()));
    }

    public ItemStack getBanner(DyeColor main, DyeColor back, Registry<BannerPattern> lookup) {
        BannerBlock block = (BannerBlock) BannerBlock.getForColor(back);
        ItemStack stack = block.asItem().getDefaultStack();
        List<BannerPatternsComponent.Layer> layers = new ArrayList<>();

        for (BannerJSONPattern pt : patterns)
            layers.add(new BannerPatternsComponent.Layer(pt.getPattern(lookup), pt.getColor() == 0 ? back : main));

        stack.set(DataComponentTypes.BANNER_PATTERNS, new BannerPatternsComponent(layers));
        return stack;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            return this.hashCode() == obj.hashCode();
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(patterns);
    }
}
