package vnj.vnjeconomymod.common.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;

public class MarketBreakingTool extends Item {
    public MarketBreakingTool() {
        setRegistryName("marketbreakingtool");
        setTranslationKey(VnjEconomyMod.MOD_ID + ".marketbreakingtool");
    }
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "normal"));
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return world.getTileEntity(pos) instanceof MarketBaseTile;
    }
}
