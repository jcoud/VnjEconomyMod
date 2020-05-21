package vnj.vnjeconomymod.network.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.network.AbstractMessage;
import vnj.vnjeconomymod.utils.VnjUtil;

import java.io.IOException;

public class MsgVnjemsUserRequests extends AbstractMessage.AbstractServerMessage<MsgVnjemsUserRequests>  {
    private long balance;

    public MsgVnjemsUserRequests() {}

    public MsgVnjemsUserRequests(long balance) {
        this.balance = balance;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        balance = buffer.readLong();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeLong(balance);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (!VnjUtil.FromApi.isVnjemsLoaded()) {
            VnjEconomyMod.logger.warn("[net] VnjEmsApi is not loaded!");
            return;
        }
        boolean b = VnjUtil.FromApi.updateUser(player.getName(), balance);
        if (b) VnjEconomyMod.logger.info("[net] User's ({}) balance {} was successfully updated", player.getName(), balance);
        else VnjEconomyMod.logger.warn("[net] User's ({}) balance {} has not been updated", player.getName(), balance);
    }
}
