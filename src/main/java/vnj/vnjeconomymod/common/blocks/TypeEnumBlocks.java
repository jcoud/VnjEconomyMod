package vnj.vnjeconomymod.common.blocks;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import vnj.vnjeconomymod.VnjEconomyMod;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.common.tileEntities.MarketBuyerTE;
import vnj.vnjeconomymod.common.tileEntities.MarketSellerTE;
import vnj.vnjeconomymod.common.tileEntities.MarketTraderTE;

public enum TypeEnumBlocks implements IStringSerializable {
    //EMPTY("", "market", null),
    BUYER("marketbuyer", MarketBuyerTE.class),
    SELLER("marketseller", MarketSellerTE.class),
    TRADER("markettrader", MarketTraderTE.class);

    private String resourceName;
    private Class<? extends MarketBaseTile> tileClass;

    TypeEnumBlocks(String resourceName, Class<? extends MarketBaseTile> tileClass){
        this.resourceName = resourceName;
        this.tileClass = tileClass;
    }
    public Class<? extends MarketBaseTile> getTileClass() {
        return tileClass;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(VnjEconomyMod.MOD_ID, String.format("textures/gui/%s.png", resourceName));
    }

    public boolean isTrader() {
        return this == TRADER;
    }

    public boolean isBuyer() {
        return this == BUYER;
    }

    public boolean isSeller() {
        return this == SELLER;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public static void setupCreativeTab() {
        BlockHolder.market.setCreativeTab(VnjEconomyMod.vnjtab);
    }


    public MarketBaseTile createTile() {
        switch (this) {
            case BUYER: return new MarketBuyerTE();
            case SELLER: return new MarketSellerTE();
            case TRADER: return new MarketTraderTE();
            //case EMPTY:
            default: return null;
        }
    }
}