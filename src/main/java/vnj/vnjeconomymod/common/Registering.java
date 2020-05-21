package vnj.vnjeconomymod.common;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vnj.vnjeconomymod.common.blocks.BlockHolder;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;
import vnj.vnjeconomymod.common.items.ItemMarket;

public class Registering {

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for (TypeEnumBlocks type : TypeEnumBlocks.values()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockHolder.market), type.ordinal(), new ModelResourceLocation(BlockHolder.market.getRegistryName(), "type=" + type.getName()));
        }
    }
    public static void registerTiles() {
        for (TypeEnumBlocks type : TypeEnumBlocks.values()) if (type.getTileClass() != null)
            GameRegistry.registerTileEntity(type.getTileClass(), new ResourceLocation("vnjeconomymod", type.getName()));
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(BlockHolder.market);

    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemMarket(BlockHolder.market));
    }
}