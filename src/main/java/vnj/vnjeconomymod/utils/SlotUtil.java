package vnj.vnjeconomymod.utils;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vnj.vnjeconomymod.common.inventory.MSlotIn;
import vnj.vnjeconomymod.common.inventory.SlotDisplayOnly;

public abstract class SlotUtil {

    public static boolean isSlotInRange(int slotIndex, int start, int count) {
        return slotIndex >= start && slotIndex < start + count;
    }


    public static ItemStack transferStackInSlot(List<Slot> inventorySlots, EntityPlayer player, int slotIndex) {
        Slot slot = inventorySlots.get(slotIndex);
//        if (slot == null || !slot.getHasStack() ||
//            (slot instanceof MSlotIn && !((MSlotIn)slot).canInsert()) ||
//            slot instanceof SlotDisplayOnly) {
//            return ItemStack.EMPTY;
//        }


        int numSlots = inventorySlots.size();
        ItemStack stackInSlot = slot.getStack();
        ItemStack originalStack = stackInSlot.copy();

        if (!shiftItemStack(inventorySlots, stackInSlot, slotIndex, numSlots)) {
            return ItemStack.EMPTY;
        }

        slot.onSlotChange(stackInSlot, originalStack);
        if (stackInSlot.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (stackInSlot.getCount() == originalStack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stackInSlot);
        return originalStack;
    }

    private static boolean shiftItemStack(List<Slot> inventorySlots, ItemStack stackInSlot, int slotIndex, int numSlots) {
        if (isInPlayerInventory(slotIndex)) {
            if (shiftToMachineInventory(inventorySlots, stackInSlot, numSlots)) {
                return true;
            }

            if (isInPlayerHotbar(slotIndex)) {
                return shiftToPlayerInventoryNoHotbar(inventorySlots, stackInSlot);
            } else {
                return shiftToHotbar(inventorySlots, stackInSlot);
            }
        } else {
            return shiftToPlayerInventory(inventorySlots, stackInSlot);
        }

    }

    private static boolean shiftItemStackToRange(List<Slot> inventorySlots, ItemStack stackToShift, int start, int count) {
        boolean changed = shiftItemStackToRangeMerge(inventorySlots, stackToShift, start, count);
        changed |= shiftItemStackToRangeOpenSlots(inventorySlots, stackToShift, start, count);
        return changed;
    }

    private static boolean shiftItemStackToRangeMerge(List<Slot> inventorySlots, ItemStack stackToShift, int start, int count) {
        if (!stackToShift.isStackable() || stackToShift.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (int slotIndex = start; !stackToShift.isEmpty() && slotIndex < start + count; slotIndex++) {
            Slot slot = inventorySlots.get(slotIndex);
            ItemStack stackInSlot = slot.getStack();
            if (!stackInSlot.isEmpty() && ItemStackUtil.isIdenticalItem(stackInSlot, stackToShift)) {
                int resultingStackSize = stackInSlot.getCount() + stackToShift.getCount();
                int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                if (resultingStackSize <= max) {
                    stackToShift.setCount(0);
                    stackInSlot.setCount(resultingStackSize);
                    slot.onSlotChanged();
                    changed = true;
                } else if (stackInSlot.getCount() < max) {
                    stackToShift.shrink(max - stackInSlot.getCount());
                    stackInSlot.setCount(max);
                    slot.onSlotChanged();
                    changed = true;
                }
            }
        }
        return changed;
    }

    public static boolean shiftItemStackToRangeOpenSlots(List<Slot> inventorySlots, ItemStack stackToShift, int start, int count) {
        if (stackToShift.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (int slotIndex = start; !stackToShift.isEmpty() && slotIndex < start + count; slotIndex++) {
            Slot slot = inventorySlots.get(slotIndex);
            ItemStack stackInSlot = slot.getStack();
            if (stackInSlot.isEmpty()) {
                int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
                stackInSlot = stackToShift.copy();
                stackInSlot.setCount(Math.min(stackToShift.getCount(), max));
                stackToShift.shrink(stackInSlot.getCount());
                slot.putStack(stackInSlot);
                slot.onSlotChanged();
                changed = true;
            }
        }
        return changed;
    }

    private static final int playerInventorySize = 9 * 4;
    private static final int playerHotbarSize = 9;

    private static boolean isInPlayerInventory(int slotIndex) {
        return slotIndex < playerInventorySize;
    }

    private static boolean isInPlayerHotbar(int slotIndex) {
        return SlotUtil.isSlotInRange(slotIndex, playerInventorySize - playerHotbarSize, playerInventorySize);
    }

    private static boolean shiftToPlayerInventory(List<Slot> inventorySlots, ItemStack stackInSlot) {
        int playerHotbarStart = playerInventorySize - playerHotbarSize;

        // try to merge with existing stacks, hotbar first
        boolean shifted = shiftItemStackToRangeMerge(inventorySlots, stackInSlot, playerHotbarStart, playerHotbarSize);
        shifted |= shiftItemStackToRangeMerge(inventorySlots, stackInSlot, 0, playerHotbarStart);

        // shift to open slots, hotbar first
        shifted |= shiftItemStackToRangeOpenSlots(inventorySlots, stackInSlot, playerHotbarStart, playerHotbarSize);
        shifted |= shiftItemStackToRangeOpenSlots(inventorySlots, stackInSlot, 0, playerHotbarStart);
        return shifted;
    }

    private static boolean shiftToPlayerInventoryNoHotbar(List<Slot> inventorySlots, ItemStack stackInSlot) {
        int playerHotbarStart = playerInventorySize - playerHotbarSize;
        return shiftItemStackToRange(inventorySlots, stackInSlot, 0, playerHotbarStart);
    }

    private static boolean shiftToHotbar(List<Slot> inventorySlots, ItemStack stackInSlot) {
        int playerHotbarStart = playerInventorySize - playerHotbarSize;
        return shiftItemStackToRange(inventorySlots, stackInSlot, playerHotbarStart, playerHotbarSize);
    }

    private static boolean shiftToMachineInventory(List<Slot> inventorySlots, ItemStack stackToShift, int numSlots) {
        boolean success = false;
        if (stackToShift.isStackable()) {
            success = shiftToMachineInventory(inventorySlots, stackToShift, numSlots, true);
        }
        if (!stackToShift.isEmpty()) {
            success |= shiftToMachineInventory(inventorySlots, stackToShift, numSlots, false);
        }
        return success;
    }

    // if mergeOnly = true, don't shift into empty slots.
    private static boolean shiftToMachineInventory(List<Slot> inventorySlots, ItemStack stackToShift, int numSlots, boolean mergeOnly) {
        for (int machineIndex = playerInventorySize; machineIndex < numSlots; machineIndex++) {
            Slot slot = inventorySlots.get(machineIndex);
            if (mergeOnly && slot.getStack().isEmpty()) {
                continue;
            }
            if (!slot.isItemValid(stackToShift)) {
                continue;
            }
            if (shiftItemStackToRange(inventorySlots, stackToShift, machineIndex, 1)) {
                return true;
            }
        }
        return false;
    }
}

