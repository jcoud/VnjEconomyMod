package vnj.vnjeconomymod.objects.ingame.blocks.testcotainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TestContainer extends Container {
    //private TestContainerTileEntity te;
    static int yOffset = 20;
    static int yOffsetMax = 152;
    static int xOffset = 8;
    private int spacing = 18;
    private int numRows;
    private IInventory playerInventory;
    private EntityPlayer playerIn;
    private IInventory selfInventory; /*= new TestContainerInventory("Test Container", true, 10);*/

    public TestContainer(IInventory playerInventory,  IInventory selfInventory, EntityPlayer playerIn) {
        //this.te = te;
        this.playerInventory = playerInventory;
        this.selfInventory = selfInventory;
        this.playerIn = playerIn;
        this.numRows = selfInventory.getSizeInventory() / 9;
        selfInventory.openInventory(playerIn);

        // This container references items out of our own inventory (the 9 slots we hold ourselves)
        // as well as the slots from the player inventory so that the user can transfer items between
        // both inventories. The two calls below make sure that slots are defined for both inventories.
        addPlayerSlots(addOwnSlots(yOffset));
    }
    private int addOwnSlots(int i) {
        //IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // Add our own slots
        int slotIndex = 0;
        int x = xOffset;
        for (int a = 0; a < selfInventory.getSizeInventory(); a++) {
            addSlotToContainer(new Slot(selfInventory, slotIndex, x, i));
            slotIndex++;
            x += 18;
        }

//        addSlotToContainer(new Slot(selfInventory, slotIndex, xOffset, i));
//        addSlotToContainer(new Slot(selfInventory, slotIndex+1, xOffset + spacing, i));
        return i;
    }

    private int addPlayerSlots(int i) {
        // Slots for the main inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slot = 9 + row * 9 + col;
                int x = xOffset + col * spacing;
                i = row * spacing + 51;
                this.addSlotToContainer(new Slot(playerInventory, slot, x, i));
            }
        }

        // Slots for the hotbar
        i += 22;
        for (int row = 0; row < 9; row++) {
            int x = xOffset + row * spacing;
            this.addSlotToContainer(new Slot(playerInventory, row, x, i));
        }
        return i;
    }

    public IInventory getPlayerInventory(){
        return playerInventory;
    }
    public IInventory getSelfInventory() {return  selfInventory;}

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TestContainerTileEntity.SIZE) {
                if (!this.mergeItemStack(itemstack1, TestContainerTileEntity.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TestContainerTileEntity.SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return selfInventory.isUsableByPlayer(playerIn);
    }
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.selfInventory.closeInventory(playerIn);
    }
}
