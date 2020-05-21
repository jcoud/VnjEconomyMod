package vnj.vnjeconomymod.common.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;

import javax.annotation.Nonnull;

public abstract class InvHelper extends ItemStackHandler {
    public MarketBaseTile source;
    private String name;

    public InvHelper(MarketBaseTile source, int size, String name) {
        super(size);
        this.source = source;
        this.name = name;
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        super.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return super.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return super.getSlotLimit(slot);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return super.getStackLimit(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    protected void validateSlotIndex(int slot) {
        super.validateSlotIndex(slot);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        source.markDirty();
    }

    public String getInvName() {
        return name;
    }

    public String getLocalizedInvName() {
        return I18n.format("gui.inventory." + name + ".name");
    }

    public static void dropItems(World worldIn, BlockPos pos, IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i));
        }
    }
}
