package com.mrg.banneralphabet.mixin;

import com.mrg.banneralphabet.gui.BannerPanel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class CreativeMenuMixin extends Screen {

    protected CreativeMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init()V")
    protected void init(CallbackInfo ci) {
        if (this.client == null) return;

        int buttonWidth = 20;
        int buttonHeight = 20;
        int x = buttonWidth/2;
        int y = buttonHeight/2;

        ButtonWidget menuButton = ButtonWidget.builder(
                        Text.of(""),
                        button -> this.client.setScreen(new BannerPanel(this, MinecraftClient.getInstance().player)))
                .dimensions(x, y, buttonWidth, buttonHeight)
                .build();

        int delta = 2;
        Drawable banner = new Drawable() {
            ItemStack itemStack = new ItemStack(Items.CREEPER_BANNER_PATTERN);
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float d) {
                context.drawItem(itemStack, x+delta, y+delta, buttonWidth, buttonHeight);
            }
        };

        this.addDrawableChild(menuButton);
        this.addDrawable(banner);
    }
}
