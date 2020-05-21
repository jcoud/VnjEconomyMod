package vnj.vnjeconomymod.common.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vnj.vnjeconomymod.common.inventory.InvHelper;
import vnj.vnjeconomymod.common.inventory.MSlotIn;
import vnj.vnjeconomymod.common.inventory.MSlotOut;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.common.inventory.SlotDisplayOnly;
import vnj.vnjeconomymod.utils.ItemStackUtil;
import vnj.vnjeconomymod.utils.SlotUtil;

import java.util.ArrayList;
import java.util.List;

public class ContainerBase extends Container {
    public static final int SPACING = 18;
    public static final int X_OFFSET = 8;
    public static final int Y_UPPER_CONTAINER_NAME = 6;
    public static final int Y_CONTAINER_INV_SLOTS = 47;
    public static final int Y_DISPLAY_SLOTS = 11;
    public static final int X_DISPLAY_HAVE_SLOT = X_OFFSET + SPACING * 3;
    public static final int X_DISPLAY_WANT_SLOT = X_OFFSET + SPACING * 5;
    public static int Y_LOWER_CONTAINER_NAME;
    public static int Y_PLAYER_INV_SLOTS;
    public static int Y_PLAYER_HOTBAR_SLOTS;

    private IInventory playerInventory;
    private InvHelper marketInvIn, marketInvOut, fakeInventory;
    private MarketBaseTile source;

    public ContainerBase(MarketBaseTile tile, EntityPlayer player) {
        this.source = tile;
        Y_LOWER_CONTAINER_NAME = source.getMarketBlockType().isTrader() ? 126 : 85;
        Y_PLAYER_INV_SLOTS = source.getMarketBlockType().isTrader() ? 136 : 96;
        Y_PLAYER_HOTBAR_SLOTS = source.getMarketBlockType().isTrader() ? 194 : 154;

        this.playerInventory = player.inventory;
        this.fakeInventory = tile.getDisplayInv();
        this.marketInvIn = source.getMarketBlockType().isSeller() || source.getMarketBlockType().isTrader() ? source.getInventoryInput() : null;
        this.marketInvOut = source.getMarketBlockType().isBuyer() || source.getMarketBlockType().isTrader() ? source.getInventoryOutput() : null;
//        this.inventoryIn.openInventory(player);
        addPlayerInventory();
        addOwnSlots();
        addDisplaySlot();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return source.canInteractWith(playerIn);
    }

    @Override
    public void setAll(List<ItemStack> p_190896_1_) {
        super.setAll(p_190896_1_);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        super.putStackInSlot(slotID, stack);
    }

    private void addPlayerInventory() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = X_OFFSET + j * SPACING;
                int y = Y_PLAYER_INV_SLOTS + i * SPACING;
                int slot = i * 9 + j + 9;
                addSlotToContainer(new Slot(playerInventory, slot, x, y));
            }
        }
        for (int i = 0; i < 9; i++) {
            int x = X_OFFSET + i * SPACING;
            int y = Y_PLAYER_HOTBAR_SLOTS;
            int slot = i;
            addSlotToContainer(new Slot(playerInventory, slot, x, y));
        }
    }

    private void addOwnSlots() {
        if (marketInvIn != null) {
            for (int row = 0; row < marketInvIn.getSlots() / 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int slot = row * 9 + col;
                    int x = col * SPACING + X_OFFSET;
                    int y = row * SPACING + Y_CONTAINER_INV_SLOTS;
                    addSlotToContainer(new MSlotIn(marketInvIn, slot, x, y));
                }
            }
        }
        if (marketInvOut != null) {
            for (int row = 0; row < marketInvOut.getSlots() / 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int slot = row * 9 + col;
                    int x = col * SPACING + X_OFFSET;
                    int y = row * SPACING + Y_CONTAINER_INV_SLOTS + (source.getMarketBlockType().isTrader() ? 40 : 0);
                    addSlotToContainer(new MSlotOut(marketInvOut, slot, x, y));
                }
            }
        }
    }

    private void addDisplaySlot() {
        switch (source.getMarketBlockType()) {
            case SELLER:
                addSlotToContainer(new SlotDisplayOnly(fakeInventory, 0, X_DISPLAY_HAVE_SLOT + SPACING, Y_DISPLAY_SLOTS));
                break;
            case BUYER:
                addSlotToContainer(new SlotDisplayOnly(fakeInventory, 0, X_DISPLAY_WANT_SLOT - SPACING, Y_DISPLAY_SLOTS));
                break;
            case TRADER:
                addSlotToContainer(new SlotDisplayOnly(fakeInventory, 0, X_DISPLAY_HAVE_SLOT, Y_DISPLAY_SLOTS));
                addSlotToContainer(new SlotDisplayOnly(fakeInventory, 1, X_DISPLAY_WANT_SLOT, Y_DISPLAY_SLOTS));
                break;
        }
    }


    @Override
    public ItemStack slotClick(int slotId, int mouseButton, ClickType modifier, EntityPlayer player) {

        Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
        if (slot == null) return super.slotClick(slotId, mouseButton, modifier, player);

        if (slot instanceof SlotDisplayOnly) {
            if (ItemStackUtil.isIdenticalItem(slot.getStack(), player.inventory.getItemStack())) {
                if (mouseButton == 0) {
                    slot.getStack().setCount(MathHelper.clamp(slot.getStack().getCount() + 1, 1, 64));
                }
                else if (mouseButton == 1) {
                    slot.getStack().setCount(MathHelper.clamp(slot.getStack().getCount() - 1, 1, 64));
                }
                else if (mouseButton == 2) {
                    slot.getStack().setCount(player.inventory.getItemStack().getCount());
                }
            }
            else {
                slot.putStack(player.inventory.getItemStack().isEmpty() ? ItemStack.EMPTY : player.inventory.getItemStack().copy());
                slot.getStack().setCount(1);
            }
            return player.inventory.getItemStack();
        }
        else if (slot instanceof MSlotOut) {
            if (modifier == ClickType.PICKUP) return super.slotClick(slotId, mouseButton, modifier, player);
            else return player.inventory.getItemStack();
        }
        return super.slotClick(slotId, mouseButton, modifier, player);
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        if (!canInteractWith(player)) return ItemStack.EMPTY;
//        if (inventorySlots.get(slotIndex) instanceof MSlotOut) {
//
//        }
        List<Slot> ls = Lists.newArrayList();
        for (Slot s : inventorySlots) {
            if (s instanceof SlotDisplayOnly || s instanceof MSlotOut) continue;
            ls.add(s);
        }
        return SlotUtil.transferStackInSlot(ls, player, slotIndex);
//        return SlotUtil.transferStackInSlot(inventorySlots, player, slotIndex);
    }
}