package vnj.vnjeconomymod.objects.ingame.blocks.testcotainer;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vnj.vnjeconomymod.VnjEconomyMod;

public class TestContainerBlock extends BlockContainer implements ITileEntityProvider {
    public static final int GUI_ID = 1;
    public static final String BLOCKNAME = "testcontainerblock";

    public TestContainerBlock() {
        super(Material.GLASS);
        this.translucent = true;
        this.setLightOpacity(1);
        this.setUnlocalizedName(String.format("%s.%s", VnjEconomyMod.MOD_ID, BLOCKNAME));
        this.setRegistryName(new ResourceLocation(String.format("%s:%s", VnjEconomyMod.MOD_ID, BLOCKNAME)));
    }
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }


    //    @SideOnly(Side.CLIENT)
//    public void initModel() {
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//    }
    @NotNull
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TestContainerTileEntity();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TestContainerTileEntity tileentity = (TestContainerTileEntity) worldIn.getTileEntity(pos);

        if (tileentity != null ){
            InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        // Only execute on the server
        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TestContainerTileEntity)) {
            return false;
        }

        //player.displayGUIChest((IInventory) te);
        player.openGui(VnjEconomyMod.INSTANCE, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

}
