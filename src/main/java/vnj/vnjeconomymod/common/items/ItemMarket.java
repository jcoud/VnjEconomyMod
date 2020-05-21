package vnj.vnjeconomymod.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.common.blocks.MarketBaseBlock;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;

import javax.annotation.Nonnull;

public class ItemMarket extends ItemBlock {
    public ItemMarket(MarketBaseBlock block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        if (stack.getMetadata() < TypeEnumBlocks.values().length) {
            return String.format("tile.%s.market.%s", VnjEconomyMod.MOD_ID, TypeEnumBlocks.values()[stack.getMetadata()].getName());
        }
        else {
            return super.getTranslationKey(stack);
        }

    }
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (TypeEnumBlocks type : TypeEnumBlocks.values()) {
            if (this.isInCreativeTab(tab)) {
                items.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }
}
