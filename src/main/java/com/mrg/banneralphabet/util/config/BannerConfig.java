package com.mrg.banneralphabet.util.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mrg.banneralphabet.BannerAlphabet;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BannerConfig {
    private static final Path JSON_PATH = Path.of( "config/" + BannerAlphabet.MOD_ID + ".json");
    private static final String DEFAULT_CONFIG = "[{\"patterns\":[{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:curly_border\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:curly_border\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":0},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal\",\"color\":0},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":0},{\"pattern\":\"minecraft:stripe_right\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":1}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_center\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal\",\"color\":0},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_downright\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal\",\"color\":0},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:triangle_top\",\"color\":1},{\"pattern\":\"minecraft:triangles_top\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:triangle_top\",\"color\":0},{\"pattern\":\"minecraft:stripe_downright\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal_bottom\",\"color\":0},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:rhombus\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":0},{\"pattern\":\"minecraft:square_bottom_right\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":1}]},{\"patterns\":[{\"pattern\":\"minecraft:half_horizontal\",\"color\":1},{\"pattern\":\"minecraft:stripe_center\",\"color\":0},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_downright\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:rhombus\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:stripe_downright\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":1}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_center\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:triangle_bottom\",\"color\":0},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:triangle_bottom\",\"color\":1},{\"pattern\":\"minecraft:triangles_bottom\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:cross\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_downright\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal_bottom\",\"color\":0},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[]},{\"patterns\":[]},{\"patterns\":[{\"pattern\":\"minecraft:rhombus\",\"color\":1},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":0},{\"pattern\":\"minecraft:stripe_right\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":1}]},{\"patterns\":[{\"pattern\":\"minecraft:square_top_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_center\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:rhombus\",\"color\":0},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal_bottom\",\"color\":0},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:rhombus\",\"color\":0},{\"pattern\":\"minecraft:stripe_downright\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal_bottom\",\"color\":1},{\"pattern\":\"minecraft:rhombus\",\"color\":0},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:straight_cross\",\"color\":1},{\"pattern\":\"minecraft:triangle_bottom\",\"color\":0},{\"pattern\":\"minecraft:triangle_top\",\"color\":0},{\"pattern\":\"minecraft:stripe_downleft\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:half_horizontal\",\"color\":1},{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:rhombus\",\"color\":0},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[]},{\"patterns\":[]},{\"patterns\":[{\"pattern\":\"minecraft:square_bottom_left\",\"color\":1},{\"pattern\":\"minecraft:diagonal_left\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":0},{\"pattern\":\"minecraft:triangles_bottom\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":0},{\"pattern\":\"minecraft:stripe_right\",\"color\":0},{\"pattern\":\"minecraft:creeper\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_bottom\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal\",\"color\":1},{\"pattern\":\"minecraft:stripe_left\",\"color\":0},{\"pattern\":\"minecraft:stripe_right\",\"color\":0},{\"pattern\":\"minecraft:creeper\",\"color\":0},{\"pattern\":\"minecraft:border\",\"color\":0}]},{\"patterns\":[{\"pattern\":\"minecraft:stripe_top\",\"color\":1},{\"pattern\":\"minecraft:stripe_right\",\"color\":1},{\"pattern\":\"minecraft:half_horizontal_bottom\",\"color\":0},{\"pattern\":\"minecraft:stripe_middle\",\"color\":1},{\"pattern\":\"minecraft:square_bottom_left\",\"color\":1},{\"pattern\":\"minecraft:border\",\"color\":0}]}]";

    private static List<BannerJSONElement> banners;

    public static void serialize() {
        Gson gson = new Gson();
        String jsonResult = gson.toJson(banners);
        try {
            if (Files.exists(JSON_PATH))
                Files.writeString(JSON_PATH, jsonResult);
            else
                Files.writeString(JSON_PATH, DEFAULT_CONFIG);
            BannerAlphabet.LOGGER.info("Banner alphabet config saved!");
        } catch (IOException e) {
            BannerAlphabet.LOGGER.warn("Can't save banner configuration! Please do it manualy:\n{}", jsonResult);
        }
    }
    public static void deserialize() {
        Gson gson = new Gson();
        try {
            String jsonResult = Files.readString(JSON_PATH);
            banners = gson.fromJson(jsonResult, new TypeToken<List<BannerJSONElement>>() {}.getType());
            if (banners == null) banners = new ArrayList<>();
            BannerAlphabet.LOGGER.info("Banner alphabet config loaded!");
        } catch (IOException | JsonSyntaxException ex) {
            BannerAlphabet.LOGGER.warn("Can't lode banner configuration! Loading default config...");
            banners = gson.fromJson(DEFAULT_CONFIG, new TypeToken<List<BannerJSONElement>>() {}.getType());
        }
    }

    public static boolean addBanner(ItemStack banner) {
        BannerJSONElement el = new BannerJSONElement(banner);
        if (!banners.contains(el)) {
            banners.add(el);
            return true;
        }
        return false;
    }
    public static boolean remBanner(ItemStack banner) {
        BannerJSONElement el = new BannerJSONElement(banner);
        if (banners.contains(el)) {
            banners.remove(el);
            return true;
        }
        return false;
    }
    public static boolean replaceBanner(ItemStack oldBanner, ItemStack newBanner) {
        BannerJSONElement old_ = new BannerJSONElement(oldBanner);
        BannerJSONElement new_ = new BannerJSONElement(newBanner);
        int index = banners.indexOf(old_);
        if (index != -1 && !banners.contains(new_)) {
            banners.set(index, new_);
            return true;
        }
        return false;
    }

    public static List<ItemStack> getBanners(DyeColor main, DyeColor back) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) throw new RuntimeException("Client player entity doesn't initialized");
        RegistryEntryLookup<BannerPattern> lookup = player.getRegistryManager().getOrThrow(RegistryKeys.BANNER_PATTERN);

        List<ItemStack> list = new ArrayList<>(List.of());
        for (BannerJSONElement el : banners) {
            list.add(el.getBanner(main, back, lookup));
        }

        return list;
    }
}
