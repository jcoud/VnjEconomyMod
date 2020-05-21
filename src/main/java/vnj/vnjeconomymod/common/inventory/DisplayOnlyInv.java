package vnj.vnjeconomymod.common.inventory;

import net.minecraft.item.ItemStack;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;

import javax.annotation.Nonnull;


public class DisplayOnlyInv extends InvHelper {

    public DisplayOnlyInv(MarketBaseTile source, int size, String name) {
        super(source, size, name);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (source.getMarketBlockType().isSeller()) return stacks.get(0);
        if (source.getMarketBlockType().isBuyer()) return stacks.get(1);
        return stacks.get(index);
//        return super.getStackInSlot(index);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (source.getMarketBlockType().isSeller()) { super.setStackInSlot(0, stack); return; }
        if (source.getMarketBlockType().isBuyer()) { super.setStackInSlot(1, stack); return; }
        super.setStackInSlot(slot, stack);
//        source.markDirty();
    }


    public ItemStack getItemHave() {
        return stacks.get(0);
    }

    public ItemStack getItemWant() {
        return stacks.get(1);
    }

}
