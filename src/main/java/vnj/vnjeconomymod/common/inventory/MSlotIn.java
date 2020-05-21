package vnj.vnjeconomymod.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MSlotIn extends SlotItemHandler {
    public MSlotIn(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return super.decrStackSize(amount);
    }


    @Override
    public void putStack(ItemStack stack) {
        super.putStack(stack);
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        return super.onTake(thePlayer, stack);
    }

    @Override
    public boolean isHere(IInventory inv, int slotIn) {
        return super.isHere(inv, slotIn);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return super.canTakeStack(playerIn);
    }
}
