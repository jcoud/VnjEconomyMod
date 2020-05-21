package vnj.vnjeconomymod.common.items;

import vnj.vnjeconomymod.VnjEconomyMod;

public class ItemHolder {
    public static MarketBreakingTool marketBreakingTool = new MarketBreakingTool();

    public static void initModels() {
        marketBreakingTool.initModel();
    }
    public static void setupCreativeTab() {
        marketBreakingTool.setCreativeTab(VnjEconomyMod.vnjtab);
    }
}
