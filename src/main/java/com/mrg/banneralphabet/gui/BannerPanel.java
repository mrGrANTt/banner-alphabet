package com.mrg.banneralphabet.gui;

import com.mrg.banneralphabet.BannerAlphabet;
import com.mrg.banneralphabet.util.BannerMenuColors;
import com.mrg.banneralphabet.util.config.BannerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BannerPanel extends HandledScreen<BannerPanel.BannerScreenHandler> {
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller_disabled");
    private static final Identifier MENU_TEXTURE = Identifier.tryParse(BannerAlphabet.MOD_ID, "/textures/menu.png");
    private static final Identifier SELECTED_SLOT = Identifier.tryParse(BannerAlphabet.MOD_ID, "/textures/selected_slot.png");

    private Screen parent;
    static final SimpleInventory INVENTORY = new SimpleInventory(20);

    private float scrollPosition;
    private boolean scrolling;

    @Override
    public void blur() {}

    public BannerPanel(Screen parent, ClientPlayerEntity player) {
        super(new BannerPanel.BannerScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
        player.currentScreenHandler = this.handler;
        this.backgroundHeight = 136;
        this.backgroundWidth = 195;
        this.parent = parent;
        playerInventoryTitleY = -MinecraftClient.getInstance().getWindow().getHeight()/2;
    }

    @Override
    protected void drawBackground(DrawContext dc, float delta, int mx, int my) {
        dc.drawTexture(RenderLayer::getGuiTextured, MENU_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        dc.drawTexture(RenderLayer::getGuiTextured, SELECTED_SLOT, this.x + 9 + BannerScreenHandler.mainColor.getX()*10, this.y + 35 + BannerScreenHandler.mainColor.getY()*10, 0.0F, 0.0F, 10, 10, 10, 10);
        dc.drawTexture(RenderLayer::getGuiTextured, SELECTED_SLOT, this.x + 9 + BannerScreenHandler.backColor.getX()*10,  this.y + 71 + BannerScreenHandler.backColor.getY()*10, 0.0F, 0.0F, 10, 10, 10, 10);

        dc.drawText(textRenderer, Text.translatable("banner-alphabet:title"), this.x + 8, this.y + 6, 0xFF3F3F3F, false);
        dc.drawText(textRenderer, Text.translatable("banner-alphabet:main_color"), this.x + 8, this.y + 26, 0xFF3F3F3F, false);
        dc.drawText(textRenderer, Text.translatable("banner-alphabet:back_color"), this.x + 8, this.y + 62, 0xFF3F3F3F, false);

        int i = this.x + 175;
        int j = this.y + 18;
        int k = j + 112;
        Identifier identifier = handler.shouldShowScrollbar() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        dc.drawGuiTexture(RenderLayer::getGuiTextured, identifier, i, j + (int)((k - j - 17) * this.scrollPosition), 12, 15);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
        } else if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            this.close();
        } else {
            this.handleHotbarKeyPressed(keyCode, scanCode);
            if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
                if (this.client.options.pickItemKey.matchesKey(keyCode, scanCode)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 0, SlotActionType.CLONE);
                } else if (this.client.options.dropKey.matchesKey(keyCode, scanCode)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, hasControlDown() ? 1 : 0, SlotActionType.THROW);
                }
            }

        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        } else if (!handler.shouldShowScrollbar()) {
            return false;
        } else {
            this.scrollPosition = this.handler.getScrollPosition(this.scrollPosition, verticalAmount);
            this.handler.scrollItems(this.scrollPosition);
            return true;
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        int i = this.handler.getRow(this.scrollPosition);
        this.init(client, width, height);

        this.scrollPosition = this.handler.getScrollPosition(i);
        this.handler.scrollItems(this.scrollPosition);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.scrolling) {
            int i = this.y + 18;
            int j = i + 112;
            this.scrollPosition = ((float)mouseY - i - 7.5F) / (j - i - 15.0F);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0F, 1.0F);
            this.handler.scrollItems(this.scrollPosition);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    protected boolean handleHotbarKeyPressed(int keyCode, int scanCode) {
        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null) { //TODO: Почему то дополнительно выдает + 1 стак
            if (this.client.options.swapHandsKey.matchesKey(keyCode, scanCode)) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 40, SlotActionType.SWAP);
                return true;
            }

            for (int i = 0; i < 9; i++) {
                if (this.client.options.hotbarKeys[i].matchesKey(keyCode, scanCode)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, i, SlotActionType.SWAP);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onMouseClick(@Nullable Slot slot, int slotId, int button, SlotActionType actionType) {
        boolean bl = actionType == SlotActionType.QUICK_MOVE;
        actionType = slotId == -999 && actionType == SlotActionType.PICKUP ? SlotActionType.THROW : actionType;
        if (actionType != SlotActionType.THROW || this.client.player.canDropItems()) {
            if (slot == null && actionType != SlotActionType.QUICK_CRAFT) {
                if (!this.handler.getCursorStack().isEmpty() && slotId == -999) {
                    if (!this.client.player.canDropItems()) {
                        return;
                    }

                    if (button == 0) {
                        this.client.player.dropItem(this.handler.getCursorStack(), true);
                        CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(-1, this.handler.getCursorStack());
                        ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                        net.sendPacket(pkg);
                        this.handler.setCursorStack(ItemStack.EMPTY);
                    }

                    if (button == 1) {
                        ItemStack itemStack = this.handler.getCursorStack().split(1);
                        this.client.player.dropItem(itemStack, true);
                        CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(-1, itemStack);
                        ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                        net.sendPacket(pkg);
                    }
                }
            } else {
                if (slot != null && !slot.canTakeItems(this.client.player)) {
                    return;
                }

                if (actionType != SlotActionType.QUICK_CRAFT && slot.inventory == INVENTORY) {
                    ItemStack itemStack = this.handler.getCursorStack();
                    ItemStack itemStack2 = slot.getStack();
                    if (actionType == SlotActionType.SWAP) {
                        if (!itemStack2.isEmpty()) {
                            ItemStack st = itemStack2.copyWithCount(itemStack2.getMaxCount());
                            this.client.player.getInventory().setStack(button, st);
                            CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(button, st);
                            ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                            net.sendPacket(pkg);
                        }

                        return;
                    }

                    if (actionType == SlotActionType.CLONE) {
                        if (this.handler.getCursorStack().isEmpty() && slot.hasStack()) {
                            ItemStack itemStack3 = slot.getStack();
                            this.handler.setCursorStack(itemStack3.copyWithCount(itemStack3.getMaxCount()));
                        }

                        return;
                    }

                    if (actionType == SlotActionType.THROW) {
                        if (!itemStack2.isEmpty()) {
                            ItemStack itemStack3 = itemStack2.copyWithCount(button == 0 ? 1 : itemStack2.getMaxCount());
                            this.client.player.dropItem(itemStack3, true);
                            CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(-1, itemStack3);
                            ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                            net.sendPacket(pkg);
                        }

                        return;
                    }

                    if (!itemStack.isEmpty() && !itemStack2.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, itemStack2)) {
                        if (button == 0) {
                            if (bl) {
                                itemStack.setCount(itemStack.getMaxCount());
                            } else if (itemStack.getCount() < itemStack.getMaxCount()) {
                                itemStack.increment(1);
                            }
                        } else {
                            itemStack.decrement(1);
                        }
                    } else if (!itemStack2.isEmpty() && itemStack.isEmpty()) {
                        int j = bl ? itemStack2.getMaxCount() : itemStack2.getCount();
                        this.handler.setCursorStack(itemStack2.copyWithCount(j));
                    } else if (button == 0) {
                        this.handler.setCursorStack(ItemStack.EMPTY);
                    } else if (!this.handler.getCursorStack().isEmpty()) {
                        this.handler.getCursorStack().decrement(1);
                    }
                } else if (this.handler != null) {
                    ItemStack itemStackx = slot == null ? ItemStack.EMPTY : this.handler.getSlot(slot.id).getStack();
                    if (actionType == SlotActionType.THROW) {
                        ItemStack drop = itemStackx.split(button == 0 ? 1 : itemStackx.getCount());
                        this.client.player.dropItem(drop, true);
                        CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(-1, drop);
                        ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                        net.sendPacket(pkg);
                        this.client.player.getInventory().setStack(slotId-20, itemStackx);
                        pkg = new CreativeInventoryActionC2SPacket(slotId-20, itemStackx);
                        net = MinecraftClient.getInstance().getNetworkHandler();
                        net.sendPacket(pkg);
                    } else if (actionType == SlotActionType.QUICK_MOVE) {
                        this.client.player.getInventory().setStack(slotId-20, ItemStack.EMPTY);
                        CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(slotId-20, ItemStack.EMPTY);
                        ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                        net.sendPacket(pkg);
                    } else {
                        this.handler.onSlotClick((slot == null ? slotId : slot.id), button, actionType, this.client.player);
                        if (ScreenHandler.unpackQuickCraftStage(button) == 2) {
                            for (int k = 0; k < 9; k++) {
                                CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(36 + k, this.handler.getSlot(20 + k).getStack());
                                ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                                net.sendPacket(pkg);
                            }
                        } else if (slot != null && PlayerInventory.isValidHotbarIndex(slot.getIndex())) {
                            ItemStack stack2 = slot.getStack();
                            CreativeInventoryActionC2SPacket pkg = new CreativeInventoryActionC2SPacket(slotId, stack2);
                            ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
                            net.sendPacket(pkg);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d = mouseX - this.x;
            double e = mouseY - this.y;
            this.scrolling = false;

            if (this.isClickInScrollbar(mouseX, mouseY)) {
                this.scrolling = handler.shouldShowScrollbar();
                return true;
            }

            if (9 <= d && d < 89) {
                if (35 <= e && e < 55) {
                    BannerScreenHandler.mainColor = getClickedColor(d - 9, e - 35);
                    handler.updateBanners(scrollPosition);
                    return true;
                }
                if (71 <= e && e < 91) {
                    BannerScreenHandler.backColor = getClickedColor(d - 9, e - 71);
                    handler.updateBanners(scrollPosition);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected boolean isClickInScrollbar(double mouseX, double mouseY) {
        int i = this.x;
        int j = this.y;
        int k = i + 175;
        int l = j + 18;
        int m = k + 14;
        int n = l + 112;
        return mouseX >= k && mouseY >= l && mouseX < m && mouseY < n;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private BannerMenuColors getClickedColor(double x, double y) {
        int d = (int) x/10;
        int e = (int) y/10;

        return switch (d) {
            case 0 -> e == 0 ? BannerMenuColors.WHITE : BannerMenuColors.LIME;
            case 1 -> e == 0 ? BannerMenuColors.LIGHT_GRAY : BannerMenuColors.GREEN;
            case 2 -> e == 0 ? BannerMenuColors.GRAY : BannerMenuColors.CYAN;
            case 3 -> e == 0 ? BannerMenuColors.BLACK : BannerMenuColors.LIGHT_BLUE;
            case 4 -> e == 0 ? BannerMenuColors.BROWN : BannerMenuColors.BLUE;
            case 5 -> e == 0 ? BannerMenuColors.RED : BannerMenuColors.PURPLE;
            case 6 -> e == 0 ? BannerMenuColors.ORANGE : BannerMenuColors.MAGENTA;
            case 7 -> e == 0 ? BannerMenuColors.YELLOW : BannerMenuColors.PINK;
            default -> BannerMenuColors.WHITE;
        };
    }

    @Environment(EnvType.CLIENT)
    public static class BannerScreenHandler extends ScreenHandler {
        private static BannerMenuColors mainColor = BannerMenuColors.BLACK;
        private static  BannerMenuColors backColor = BannerMenuColors.WHITE;
        public final DefaultedList<ItemStack> bannerList = DefaultedList.of();
        private final ScreenHandler parent;

        public BannerScreenHandler(PlayerEntity player) {
            super(null, 0);
            this.parent = player.playerScreenHandler;
            PlayerInventory playerInventory = player.getInventory();

            int startX = 99,
                    startY = 18;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    this.addSlot(new BannerPanel.LockableSlot(BannerPanel.INVENTORY, i * 4 + j, startX + j*18, startY + i*18));
                }
            }

            bannerList.addAll(BannerConfig.getBanners(mainColor.getColor(), backColor.getColor()));

            this.addPlayerHotbarSlots(playerInventory, 9, 112);
            this.scrollItems(0.0F);
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }

        protected int getOverflowRows() {
            return MathHelper.ceilDiv(this.bannerList.size(), 4) - 5;
        }

        protected int getRow(float scroll) {
            return Math.max((int)(scroll * this.getOverflowRows() + 0.5), 0);
        }

        protected float getScrollPosition(int row) {
            return MathHelper.clamp((float)row / this.getOverflowRows(), 0.0F, 1.0F);
        }

        protected float getScrollPosition(float current, double amount) {
            return MathHelper.clamp(current - (float)(amount / this.getOverflowRows()), 0.0F, 1.0F);
        }

        public void scrollItems(float position) {
            int i = this.getRow(position);

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    int l = k + (j + i) * 4;
                    if (l >= 0 && l < this.bannerList.size()) {
                        BannerPanel.INVENTORY.setStack(k + j * 4, this.bannerList.get(l));
                    } else {
                        BannerPanel.INVENTORY.setStack(k + j * 4, ItemStack.EMPTY);
                    }
                }
            }
        }

        public void updateBanners(float position) {
            bannerList.clear();
            bannerList.addAll(BannerConfig.getBanners(mainColor.getColor(), backColor.getColor()));
            scrollItems(position);
        }

        public boolean shouldShowScrollbar() {
            return this.bannerList.size() > 20;
        }

        @Override
        public ItemStack quickMove(PlayerEntity player, int slot) {
            if (slot >= this.slots.size() - 4 && slot < this.slots.size()) {
                Slot slot2 = this.slots.get(slot);
                if (slot2 != null && slot2.hasStack()) {
                    slot2.setStack(ItemStack.EMPTY);
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
            return slot.inventory != BannerPanel.INVENTORY;
        }

        @Override
        public boolean canInsertIntoSlot(Slot slot) {
            return slot.inventory != BannerPanel.INVENTORY;
        }
    }

    @Environment(EnvType.CLIENT)
    static class BannerSlot extends Slot {
        final Slot slot;

        public BannerSlot(Slot slot, int invSlot, int x, int y) {
            super(slot.inventory, invSlot, x, y);
            this.slot = slot;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.slot.onTakeItem(player, stack);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return this.slot.canInsert(stack);
        }

        @Override
        public ItemStack getStack() {
            return this.slot.getStack();
        }

        @Override
        public boolean hasStack() {
            return this.slot.hasStack();
        }

        @Override
        public void setStack(ItemStack stack, ItemStack previousStack) {
            this.slot.setStack(stack, previousStack);
        }

        @Override
        public void setStackNoCallbacks(ItemStack stack) {
            this.slot.setStackNoCallbacks(stack);
        }

        @Override
        public void markDirty() {
            this.slot.markDirty();
        }

        @Override
        public int getMaxItemCount() {
            return this.slot.getMaxItemCount();
        }

        @Override
        public int getMaxItemCount(ItemStack stack) {
            return this.slot.getMaxItemCount(stack);
        }

        @Nullable
        @Override
        public Identifier getBackgroundSprite() {
            return this.slot.getBackgroundSprite();
        }

        @Override
        public ItemStack takeStack(int amount) {
            return this.slot.takeStack(amount);
        }

        @Override
        public boolean isEnabled() {
            return this.slot.isEnabled();
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return this.slot.canTakeItems(playerEntity);
        }
    }

    @Environment(EnvType.CLIENT)
    static class LockableSlot extends Slot {
        public LockableSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            ItemStack itemStack = this.getStack();
            return super.canTakeItems(playerEntity) && !itemStack.isEmpty()
                    ? itemStack.isItemEnabled(playerEntity.getWorld().getEnabledFeatures()) && !itemStack.contains(DataComponentTypes.CREATIVE_SLOT_LOCK)
                    : itemStack.isEmpty();
        }
    }
}