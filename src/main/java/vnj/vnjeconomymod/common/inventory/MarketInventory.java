package vnj.vnjeconomymod.common.inventory;

import net.minecraft.item.ItemStack;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;

import javax.annotation.Nonnull;

public class MarketInventory extends InvHelper {

    private boolean allowInput;

    public MarketInventory(MarketBaseTile source, int size, String name, boolean allowInput) {
        super(source, size, name);
        this.allowInput = allowInput;
    }


    @Nonnull
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return allowInput ? super.insertItem(slot, stack, simulate) : stack;
    }

}