package com.mrg.banneralphabet.util;

import net.minecraft.world.item.DyeColor;

public enum BannerMenuColors {
    WHITE(0,0, DyeColor.WHITE),
    LIGHT_GRAY(1,0, DyeColor.LIGHT_GRAY),
    GRAY(2,0, DyeColor.GRAY),
    BLACK(3,0, DyeColor.BLACK),
    BROWN(4,0, DyeColor.BROWN),
    RED(5,0, DyeColor.RED),
    ORANGE(6,0, DyeColor.ORANGE),
    YELLOW(7,0, DyeColor.YELLOW),
    LIME(0, 1, DyeColor.LIME),
    GREEN(1, 1, DyeColor.GREEN),
    CYAN(2, 1, DyeColor.CYAN),
    LIGHT_BLUE(3, 1, DyeColor.LIGHT_BLUE),
    BLUE(4, 1, DyeColor.BLUE),
    PURPLE(5, 1, DyeColor.PURPLE),
    MAGENTA(6, 1, DyeColor.MAGENTA),
    PINK(7, 1, DyeColor.PINK);

    private final int x;
    private final int y;
    private final DyeColor color;
    private BannerMenuColors(int x, int y, DyeColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public DyeColor getColor() { return color; }
}
