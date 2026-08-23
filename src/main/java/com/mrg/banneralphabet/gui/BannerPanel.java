package com.mrg.banneralphabet.gui;

import com.mrg.banneralphabet.BannerAlphabet;
import com.mrg.banneralphabet.util.BannerMenuColors;
import com.mrg.banneralphabet.util.config.BannerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BannerPanel extends AbstractContainerScreen<BannerPanel.BannerScreenHandler> {
    private static final Identifier SCROLLER_TEXTURE = Identifier.withDefaultNamespace("container/creative_inventory/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.withDefaultNamespace("container/creative_inventory/scroller_disabled");
    private static final Identifier MENU_TEXTURE = Identifier.tryBuild(BannerAlphabet.MOD_ID, "/textures/menu.png");
    private static final Identifier SELECTED_SLOT = Identifier.tryBuild(BannerAlphabet.MOD_ID, "/textures/selected_slot.png");

    private Screen parent;
    static final SimpleContainer INVENTORY = new SimpleContainer(20);

    private float scrollPosition;
    private boolean scrolling;

    @Override
    public void clearFocus() {}

    public BannerPanel(Screen parent, LocalPlayer player) {
        super(new BannerPanel.BannerScreenHandler(player), player.getInventory(), CommonComponents.EMPTY, 195, 136);
        player.containerMenu = this.menu;
        this.parent = parent;
        inventoryLabelY = -Minecraft.getInstance().getWindow().getScreenHeight()/2;
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor dc, final int mouseX, final int mouseY, final float a) {
        dc.blit(RenderPipelines.GUI_TEXTURED, MENU_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        dc.blit(RenderPipelines.GUI_TEXTURED, SELECTED_SLOT, this.leftPos + 9 + BannerScreenHandler.mainColor.getX()*10, this.topPos + 35 + BannerScreenHandler.mainColor.getY()*10, 0.0F, 0.0F, 10, 10, 10, 10);
        dc.blit(RenderPipelines.GUI_TEXTURED, SELECTED_SLOT, this.leftPos + 9 + BannerScreenHandler.backColor.getX()*10,  this.topPos + 71 + BannerScreenHandler.backColor.getY()*10, 0.0F, 0.0F, 10, 10, 10, 10);

        dc.text(font, Component.translatable("banner-alphabet:title"), this.leftPos + 8, this.topPos + 6, 0xFF3F3F3F, false);
        dc.text(font, Component.translatable("banner-alphabet:main_color"), this.leftPos + 8, this.topPos + 26, 0xFF3F3F3F, false);
        dc.text(font, Component.translatable("banner-alphabet:back_color"), this.leftPos + 8, this.topPos + 62, 0xFF3F3F3F, false);

        int i = this.leftPos + 175;
        int j = this.topPos + 18;
        int k = j + 112;
        Identifier identifier = menu.shouldShowScrollbar() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        dc.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, i, j + (int)((k - j - 17) * this.scrollPosition), 12, 15);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
        } else if (this.minecraft.options.keyInventory.matches(input)) {
            this.onClose();
        } else {
            this.checkHotbarKeyPressed(input);
            if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                if (this.minecraft.options.keyPickItem.matches(input)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ContainerInput.CLONE);
                } else if (this.minecraft.options.keyDrop.matches(input)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, input.hasControlDown() ? 1 : 0, ContainerInput.THROW);
                }
            }

        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        } else if (!menu.shouldShowScrollbar()) {
            return false;
        } else {
            this.scrollPosition = this.menu.getScrollPosition(this.scrollPosition, verticalAmount);
            this.menu.scrollItems(this.scrollPosition);
            return true;
        }
    }

    @Override
    public void resize(final int width, final int height) {
        int i = this.menu.getRow(this.scrollPosition);
        this.init(width, height);

        this.scrollPosition = this.menu.getScrollPosition(i);
        this.menu.scrollItems(this.scrollPosition);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        if (this.scrolling) {
            int i = this.topPos + 18;
            int j = i + 112;
            this.scrollPosition = ((float)click.y() - i - 7.5F) / (j - i - 15.0F);
            this.scrollPosition = Mth.clamp(this.scrollPosition, 0.0F, 1.0F);
            this.menu.scrollItems(this.scrollPosition);
            return true;
        } else {
            return super.mouseDragged(click, offsetX, offsetY);
        }
    }

    @Override
    protected boolean checkHotbarKeyPressed(KeyEvent input) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null) { //TODO: Почему то дополнительно выдает + 1 стак
            if (this.minecraft.options.keySwapOffhand.matches(input)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ContainerInput.SWAP);
                return true;
            }

            for (int i = 0; i < 9; i++) {
                if (this.minecraft.options.keyHotbarSlots[i].matches(input)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ContainerInput.SWAP);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void slotClicked(@Nullable Slot slot, int slotId, int button, ContainerInput actionType) {
        boolean bl = actionType == ContainerInput.QUICK_MOVE;
        actionType = slotId == -999 && actionType == ContainerInput.PICKUP ? ContainerInput.THROW : actionType;
        if (actionType != ContainerInput.THROW || this.minecraft.player.canDropItems()) {
            if (slot == null && actionType != ContainerInput.QUICK_CRAFT) {
                if (!this.menu.getCarried().isEmpty() && slotId == -999) {
                    if (!this.minecraft.player.canDropItems()) {
                        return;
                    }

                    if (button == 0) {
                        this.minecraft.player.drop(this.menu.getCarried(), true);
                        ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(-1, this.menu.getCarried());
                        ClientPacketListener net = Minecraft.getInstance().getConnection();
                        net.send(pkg);
                        this.menu.setCarried(ItemStack.EMPTY);
                    }

                    if (button == 1) {
                        ItemStack itemStack = this.menu.getCarried().split(1);
                        this.minecraft.player.drop(itemStack, true);
                        ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(-1, itemStack);
                        ClientPacketListener net = Minecraft.getInstance().getConnection();
                        net.send(pkg);
                    }
                }
            } else {
                if (slot != null && !slot.mayPickup(this.minecraft.player)) {
                    return;
                }

                if (actionType != ContainerInput.QUICK_CRAFT && slot.container == INVENTORY) {
                    ItemStack itemStack = this.menu.getCarried();
                    ItemStack itemStack2 = slot.getItem();
                    if (actionType == ContainerInput.SWAP) {
                        if (!itemStack2.isEmpty()) {
                            ItemStack st = itemStack2.copyWithCount(itemStack2.getMaxStackSize());
                            this.minecraft.player.getInventory().setItem(button, st);
                            ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(button, st);
                            ClientPacketListener net = Minecraft.getInstance().getConnection();
                            net.send(pkg);
                        }

                        return;
                    }

                    if (actionType == ContainerInput.CLONE) {
                        if (this.menu.getCarried().isEmpty() && slot.hasItem()) {
                            ItemStack itemStack3 = slot.getItem();
                            this.menu.setCarried(itemStack3.copyWithCount(itemStack3.getMaxStackSize()));
                        }

                        return;
                    }

                    if (actionType == ContainerInput.THROW) {
                        if (!itemStack2.isEmpty()) {
                            ItemStack itemStack3 = itemStack2.copyWithCount(button == 0 ? 1 : itemStack2.getMaxStackSize());
                            this.minecraft.player.drop(itemStack3, true);
                            ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(-1, itemStack3);
                            ClientPacketListener net = Minecraft.getInstance().getConnection();
                            net.send(pkg);
                        }

                        return;
                    }

                    if (!itemStack.isEmpty() && !itemStack2.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, itemStack2)) {
                        if (button == 0) {
                            if (bl) {
                                itemStack.setCount(itemStack.getMaxStackSize());
                            } else if (itemStack.getCount() < itemStack.getMaxStackSize()) {
                                itemStack.grow(1);
                            }
                        } else {
                            itemStack.shrink(1);
                        }
                    } else if (!itemStack2.isEmpty() && itemStack.isEmpty()) {
                        int j = bl ? itemStack2.getMaxStackSize() : itemStack2.getCount();
                        this.menu.setCarried(itemStack2.copyWithCount(j));
                    } else if (button == 0) {
                        this.menu.setCarried(ItemStack.EMPTY);
                    } else if (!this.menu.getCarried().isEmpty()) {
                        this.menu.getCarried().shrink(1);
                    }
                } else if (this.menu != null) {
                    ItemStack itemStackx = slot == null ? ItemStack.EMPTY : this.menu.getSlot(slot.index).getItem();
                    if (actionType == ContainerInput.THROW) {
                        ItemStack drop = itemStackx.split(button == 0 ? 1 : itemStackx.getCount());
                        this.minecraft.player.drop(drop, true);
                        ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(-1, drop);
                        ClientPacketListener net = Minecraft.getInstance().getConnection();
                        net.send(pkg);
                        this.minecraft.player.getInventory().setItem(slotId-20, itemStackx);
                        pkg = new ServerboundSetCreativeModeSlotPacket(slotId-20, itemStackx);
                        net = Minecraft.getInstance().getConnection();
                        net.send(pkg);
                    } else if (actionType == ContainerInput.QUICK_MOVE) {
                        this.minecraft.player.getInventory().setItem(slotId-20, ItemStack.EMPTY);
                        ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(slotId-20, ItemStack.EMPTY);
                        ClientPacketListener net = Minecraft.getInstance().getConnection();
                        net.send(pkg);
                    } else {
                        this.menu.clicked((slot == null ? slotId : slot.index), button, actionType, this.minecraft.player);
                        if (AbstractContainerMenu.getQuickcraftHeader(button) == 2) {
                            for (int k = 0; k < 9; k++) {
                                ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(36 + k, this.menu.getSlot(20 + k).getItem());
                                ClientPacketListener net = Minecraft.getInstance().getConnection();
                                net.send(pkg);
                            }
                        } else if (slot != null && Inventory.isHotbarSlot(slot.getContainerSlot())) {
                            ItemStack stack2 = slot.getItem();
                            ServerboundSetCreativeModeSlotPacket pkg = new ServerboundSetCreativeModeSlotPacket(slotId, stack2);
                            ClientPacketListener net = Minecraft.getInstance().getConnection();
                            net.send(pkg);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (click.button() == 0) {
            double d = click.x() - this.leftPos;
            double e = click.y() - this.topPos;
            this.scrolling = false;

            if (this.isClickInScrollbar(click.x(), click.y())) {
                this.scrolling = menu.shouldShowScrollbar();
                return true;
            }

            if (9 <= d && d < 89) {
                if (35 <= e && e < 55) {
                    BannerScreenHandler.mainColor = getClickedColor(d - 9, e - 35);
                    menu.updateBanners(scrollPosition);
                    return true;
                }
                if (71 <= e && e < 91) {
                    BannerScreenHandler.backColor = getClickedColor(d - 9, e - 71);
                    menu.updateBanners(scrollPosition);
                    return true;
                }
            }
        }

        return super.mouseClicked(click, doubled);
    }

    protected boolean isClickInScrollbar(double mouseX, double mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        int k = i + 175;
        int l = j + 18;
        int m = k + 14;
        int n = l + 112;
        return mouseX >= k && mouseY >= l && mouseX < m && mouseY < n;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        if (click.button() == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(click);
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
    public static class BannerScreenHandler extends AbstractContainerMenu {
        private static BannerMenuColors mainColor = BannerMenuColors.BLACK;
        private static  BannerMenuColors backColor = BannerMenuColors.WHITE;
        public final NonNullList<ItemStack> bannerList = NonNullList.create();
        private final AbstractContainerMenu parent;

        public BannerScreenHandler(Player player) {
            super(null, 0);
            this.parent = player.inventoryMenu;
            Inventory playerInventory = player.getInventory();

            int startX = 99,
                    startY = 18;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    this.addSlot(new BannerPanel.LockableSlot(BannerPanel.INVENTORY, i * 4 + j, startX + j*18, startY + i*18));
                }
            }

            bannerList.addAll(BannerConfig.getBanners(mainColor.getColor(), backColor.getColor()));

            this.addInventoryHotbarSlots(playerInventory, 9, 112);
            this.scrollItems(0.0F);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        protected int getOverflowRows() {
            return Mth.positiveCeilDiv(this.bannerList.size(), 4) - 5;
        }

        protected int getRow(float scroll) {
            return Math.max((int)(scroll * this.getOverflowRows() + 0.5), 0);
        }

        protected float getScrollPosition(int row) {
            return Mth.clamp((float)row / this.getOverflowRows(), 0.0F, 1.0F);
        }

        protected float getScrollPosition(float current, double amount) {
            return Mth.clamp(current - (float)(amount / this.getOverflowRows()), 0.0F, 1.0F);
        }

        public void scrollItems(float position) {
            int i = this.getRow(position);

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    int l = k + (j + i) * 4;
                    if (l >= 0 && l < this.bannerList.size()) {
                        BannerPanel.INVENTORY.setItem(k + j * 4, this.bannerList.get(l));
                    } else {
                        BannerPanel.INVENTORY.setItem(k + j * 4, ItemStack.EMPTY);
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
        public ItemStack quickMoveStack(Player player, int slot) {
            if (slot >= this.slots.size() - 4 && slot < this.slots.size()) {
                Slot slot2 = this.slots.get(slot);
                if (slot2 != null && slot2.hasItem()) {
                    slot2.setByPlayer(ItemStack.EMPTY);
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
            return slot.container != BannerPanel.INVENTORY;
        }

        @Override
        public boolean canDragTo(Slot slot) {
            return slot.container != BannerPanel.INVENTORY;
        }
    }

    @Environment(EnvType.CLIENT)
    static class BannerSlot extends Slot {
        final Slot slot;

        public BannerSlot(Slot slot, int invSlot, int x, int y) {
            super(slot.container, invSlot, x, y);
            this.slot = slot;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            this.slot.onTake(player, stack);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return this.slot.mayPlace(stack);
        }

        @Override
        public ItemStack getItem() {
            return this.slot.getItem();
        }

        @Override
        public boolean hasItem() {
            return this.slot.hasItem();
        }

        @Override
        public void setByPlayer(ItemStack stack, ItemStack previousStack) {
            this.slot.setByPlayer(stack, previousStack);
        }

        @Override
        public void set(ItemStack stack) {
            this.slot.set(stack);
        }

        @Override
        public void setChanged() {
            this.slot.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return this.slot.getMaxStackSize();
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return this.slot.getMaxStackSize(stack);
        }

        @Nullable
        @Override
        public Identifier getNoItemIcon() {
            return this.slot.getNoItemIcon();
        }

        @Override
        public ItemStack remove(int amount) {
            return this.slot.remove(amount);
        }

        @Override
        public boolean isActive() {
            return this.slot.isActive();
        }

        @Override
        public boolean mayPickup(Player playerEntity) {
            return this.slot.mayPickup(playerEntity);
        }
    }

    @Environment(EnvType.CLIENT)
    static class LockableSlot extends Slot {
        public LockableSlot(Container inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        @Override
        public boolean mayPickup(Player playerEntity) {
            ItemStack itemStack = this.getItem();
            return super.mayPickup(playerEntity) && !itemStack.isEmpty()
                    ? itemStack.isItemEnabled(playerEntity.level().enabledFeatures()) && !itemStack.has(DataComponents.CREATIVE_SLOT_LOCK)
                    : itemStack.isEmpty();
        }
    }
}