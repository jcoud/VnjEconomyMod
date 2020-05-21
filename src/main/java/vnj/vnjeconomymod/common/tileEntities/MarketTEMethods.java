package vnj.vnjeconomymod.common.tileEntities;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import vnj.vnjeconomymod.common.blocks.TypeEnumBlocks;
import vnj.vnjeconomymod.common.container.ContainerBase;
import vnj.vnjeconomymod.common.inventory.DisplayOnlyInv;
import vnj.vnjeconomymod.common.inventory.MarketInventory;

public interface MarketTEMethods {
    ContainerBase createContainer(MarketBaseTile tile, EntityPlayer player);
    GuiContainer createGui(MarketBaseTile tile, EntityPlayer player);
    long getPrice();
    String getOwner();
    DisplayOnlyInv getDisplayInv();
    MarketInventory getInventoryInput();
    MarketInventory getInventoryOutput();
    TypeEnumBlocks getMarketBlockType();
    void setOwner(String playerUUID);
    void setPrice(long l);
}
