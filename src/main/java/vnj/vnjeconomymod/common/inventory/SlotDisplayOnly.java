package vnj.vnjeconomymod.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotDisplayOnly extends SlotItemHandler {

    public SlotDisplayOnly(IItemHandler inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean getHasStack() {
        return false;
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public void putStack(ItemStack stack) {
        super.putStack(stack);

    }


    @Override
    public ItemStack decrStackSize(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isHere(IInventory inv, int slotIn) {
        return false;
    }

}