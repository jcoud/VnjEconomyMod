package vnj.vnjeconomymod.network.server;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import vnj.vnjeconomymod.network.AbstractMessage;
import vnj.vnjeconomymod.common.tileEntities.MarketTEMethods;

import java.io.IOException;

public class MsgUpdatePrice extends AbstractMessage.AbstractServerMessage<MsgUpdatePrice> {

    private long price;
    private int x, y, z;

    public MsgUpdatePrice(){}

    public MsgUpdatePrice(BlockPos pos, long price) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        this.price = price;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        price = buffer.readLong();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeLong(price);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        TileEntity te = player.world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof MarketTEMethods) {
            MarketTEMethods ite = (MarketTEMethods) te;
            ite.setPrice(price);
            IBlockState blockState = player.world.getBlockState(new BlockPos(x, y, z));
//            player.openGui(VnjEconomyMod.INSTANCE, ite.getContainerGuiId(), player.world, x, y, z);
            player.world.notifyBlockUpdate(new BlockPos(x, y, z), blockState, blockState, 3);
        }
    }
}
