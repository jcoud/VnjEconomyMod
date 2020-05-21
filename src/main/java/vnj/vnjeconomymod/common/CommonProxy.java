package vnj.vnjeconomymod.common;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.common.items.ItemHolder;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.network.PacketDispatcher;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class CommonProxy implements IGuiHandler {
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof MarketBaseTile) {
            MarketBaseTile tcte = (MarketBaseTile) te;
            return tcte.createContainer(tcte, player);
        }
        return null;
    }

    public void preInit(FMLPreInitializationEvent e) {
        PacketDispatcher.registerPackets();
//        VnjConfig.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
        VnjEconomyMod.vnjtab = new CreativeTabs(VnjEconomyMod.MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ItemHolder.marketBreakingTool);
            }
        };
        ItemHolder.setupCreativeTab();
        TypeEnumBlocks.setupCreativeTab();
    }

    public void postInit(FMLPostInitializationEvent e) {
//        VnjConfig.postInit(e);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Registering.registerBlocks(event);
        Registering.registerTiles();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ItemHolder.marketBreakingTool);
        Registering.registerItems(event);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ItemHolder.initModels();
        Registering.initModels();
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }

    public IThreadListener getThreadFromContext(MessageContext ctx) {
        return ctx.getServerHandler().player.getServerWorld();
    }


}
