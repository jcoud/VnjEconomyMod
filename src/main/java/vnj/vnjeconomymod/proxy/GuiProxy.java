package vnj.vnjeconomymod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import vnj.vnjeconomymod.objects.ingame.blocks.testcotainer.TestContainer;
import vnj.vnjeconomymod.objects.ingame.blocks.testcotainer.TestContainerGui;
import vnj.vnjeconomymod.objects.ingame.blocks.testcotainer.TestContainerTileEntity;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TestContainerTileEntity) {
            return new TestContainer(player.inventory, (IInventory) te, player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TestContainerTileEntity) {
            TestContainerTileEntity containerTileEntity = (TestContainerTileEntity) te;
            return new TestContainerGui(containerTileEntity, new TestContainer(player.inventory, (IInventory) te, player));
        }
        return null;
    }
}
