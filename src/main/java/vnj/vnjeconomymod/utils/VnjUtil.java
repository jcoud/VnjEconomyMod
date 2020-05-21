package vnj.vnjeconomymod.utils;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjems.api.IUser;
import vnj.vnjems.api.VnjDataLib;

import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VnjUtil {
    public static MarketBaseTile getMarketTileFromRayTracing(Minecraft mc) {
        if (mc == null || mc.player == null || mc.world == null) return null;
        RayTraceResult rt = mc.objectMouseOver;
        if (rt == null || rt.typeOfHit != RayTraceResult.Type.BLOCK) return null;
        TileEntity te = mc.world.getTileEntity(rt.getBlockPos());
        if (te == null) return null;
        if (!(te instanceof MarketBaseTile)) return null;
        return (MarketBaseTile) te;
    }

    public static MarketBaseTile getMarketTileFromRayTracingWithDistance(Minecraft mc, double dist) {
        MarketBaseTile mt = getMarketTileFromRayTracing(mc);
        if (mt == null) return null;
        if (mt.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) > dist * dist) return null;
        return mt;
    }

    public static Map<BlockPos, EnumFacing> getSurroundingFreeBlockPos(World currentWorld, BlockPos currentPos) {
        Map<BlockPos, EnumFacing> bp = new HashMap<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos p = new BlockPos(
                    currentPos.getX() + facing.getDirectionVec().getX(),
                    currentPos.getY() + facing.getDirectionVec().getY(),
                    currentPos.getZ() + facing.getDirectionVec().getZ()
            );
            Block b = currentWorld.getBlockState(p).getBlock();
            if (b == Blocks.AIR) bp.put(p, facing);
        }
        return bp;
    }
    public static BlockPos getClosestBlockPosFrom(Vector3f curr, List<BlockPos> byPosList) {
        Map<Float, BlockPos> m = new HashMap<>();
        for (BlockPos p : byPosList) {
            m.put(MathHelper.sqrt(
                    Math.pow(curr.getX() - p.getX(), 2) +
                    Math.pow(curr.getY() - p.getY(), 2) +
                    Math.pow(curr.getZ() - p.getZ(), 2)
            ), p);
        }
        return m.get(Collections.min(m.keySet()));
    }

    public static List<ItemStack> getContentFromInv(IItemHandler inventory) {
        List<ItemStack> content = Lists.newArrayList();
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) continue;
            content.add(inventory.getStackInSlot(i));
        }
        return content;
    }
    public static boolean isItemStackSameInContent(IItemHandler inventory, ItemStack itemStack) {
        for (ItemStack stack : getContentFromInv(inventory)) {
            if (ItemStackUtil.isIdenticalItem(stack, itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static IItemHandler makeCopy(IItemHandler inv) {
        ItemStackHandler copy = new ItemStackHandler(inv.getSlots());
        for (int i=0;i< inv.getSlots(); i++) {
            copy.setStackInSlot(i, inv.getStackInSlot(i).copy());
        }
        return copy;
    }

    public static ItemStackHandler castInvToISH(IInventory iinv) {
        ItemStackHandler casted = new ItemStackHandler(iinv.getSizeInventory());
        for (int i = 0; i < iinv.getSizeInventory(); i++) {
            casted.setStackInSlot(i, iinv.getStackInSlot(i));
        }
        return casted;
    }

    public static class FromApi {
        public static boolean isVnjemsLoaded() {
            return Loader.isModLoaded("vnjems");
        }

        @Optional.Method(modid = "vnjems")
        public static Long getUsersBalanceByName(String userName) {
            if (!isVnjemsLoaded()) return null;
            IUser iu = VnjDataLib.getUserDataByName(userName);
            if (iu == null) return null;
            return iu.getBalance();
        }
        @Optional.Method(modid = "vnjems")
        public static boolean updateUser(String userName, long balance) {
            if (!isVnjemsLoaded()) return false;
            return VnjDataLib.updateUser(userName, balance);
        }
    }
}
