package com.mrg.banneralphabet.util.config;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BannerJSONElement {
    public List<BannerJSONPattern> patterns;

    public BannerJSONElement() {
        patterns = List.of();
    }
    public BannerJSONElement(ItemStack banner) {
        BannerPatternLayers component = banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        List<BannerPatternLayers.Layer> layers = new ArrayList<>(component.layers());
        BannerItem item = (BannerItem) banner.getItem();

        patterns = new ArrayList<>();
        for (BannerPatternLayers.Layer layer : layers)
            patterns.add(new BannerJSONPattern(layer, item.getColor()));
    }

    public ItemStack getBanner(DyeColor main, DyeColor back, HolderGetter<BannerPattern> lookup) {
        ItemStack stack = color2Banner(back);
        List<BannerPatternLayers.Layer> layers = new ArrayList<>();

        for (BannerJSONPattern pt : patterns)
            layers.add(new BannerPatternLayers.Layer(pt.getPattern(lookup), pt.getColor() == 0 ? back : main));

        stack.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers(layers));
        return stack;
    }

    private ItemStack color2Banner(DyeColor back) {
        return (switch (back) {
            case WHITE -> Items.WHITE_BANNER;
            case ORANGE -> Items.ORANGE_BANNER;
            case MAGENTA -> Items.MAGENTA_BANNER;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_BANNER;
            case YELLOW -> Items.YELLOW_BANNER;
            case LIME -> Items.LIME_BANNER;
            case PINK -> Items.PINK_BANNER;
            case GRAY -> Items.GRAY_BANNER;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_BANNER;
            case CYAN -> Items.CYAN_BANNER;
            case PURPLE -> Items.PURPLE_BANNER;
            case BLUE -> Items.BLUE_BANNER;
            case BROWN -> Items.BROWN_BANNER;
            case GREEN -> Items.GREEN_BANNER;
            case RED -> Items.RED_BANNER;
            case BLACK -> Items.BLACK_BANNER;
        }).getDefaultInstance();
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
