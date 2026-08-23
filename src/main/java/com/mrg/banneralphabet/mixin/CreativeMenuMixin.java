package com.mrg.banneralphabet.mixin;

import com.mrg.banneralphabet.gui.BannerPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeMenuMixin extends Screen {

    protected CreativeMenuMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init()V")
    protected void init(CallbackInfo ci) {
        if (this.minecraft == null) return;

        int buttonWidth = 20;
        int buttonHeight = 20;
        int x = buttonWidth/2;
        int y = buttonHeight/2;

        Button menuButton = Button.builder(
                        Component.nullToEmpty(""),
                        button -> this.minecraft.setScreen(new BannerPanel(this, Minecraft.getInstance().player)))
                .bounds(x, y, buttonWidth, buttonHeight)
                .build();

        int delta = 2;
        Renderable banner = new Renderable() {
            ItemStack itemStack = new ItemStack(Items.CREEPER_BANNER_PATTERN);

            @Override
            public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
                graphics.item(itemStack, x+delta, y+delta);
            }
        };

        this.addRenderableWidget(menuButton);
        this.addRenderableOnly(banner);
    }
}
