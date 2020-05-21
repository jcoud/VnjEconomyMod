package vnj.vnjeconomymod.client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import vnj.vnjeconomymod.common.config.VnjConfig;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.utils.VnjUtil;

import java.awt.*;

public class CustomerHud extends Gui {
    private Minecraft mc;
    private HudPanel hudPanel;


    public CustomerHud(Minecraft minecraft) {
        this.mc = minecraft;
        MarketBaseTile marketTile = VnjUtil.getMarketTileFromRayTracingWithDistance(mc, 4);
        if (marketTile == null) return;

//        System.out.printf("w: %s | h: %s\r", marketTile.getFakeInv().getDisplayItemWant(), marketTile.getFakeInv().getDisplayItemHave());
        ItemStack ih = marketTile.getDisplayInv().getItemHave();
        ItemStack iw = marketTile.getDisplayInv().getItemWant();

        HudItem hih = new HudItem(ih);
        HudItem hiw = new HudItem(iw);

        HudText htih = new HudText(ih.isEmpty() ? "" : ih.getDisplayName());
        HudText htiw = new HudText(iw.isEmpty() ? "" : iw.getDisplayName());
        HudText htp  = new HudText(TextFormatting.GREEN + "" + marketTile.getPrice());
        HudText htlh = new HudText(TextFormatting.GOLD + "~gui.hud.text.typeH.name");
        HudText htlw = new HudText(TextFormatting.GOLD + "~gui.hud.text.typeW.name");
        HudText htlp = new HudText(TextFormatting.GOLD + "~gui.hud.text.price.name");

        HudEmpty empty = new HudEmpty();

        hudPanel = new HudPanel(HudComponent.VERTICAL_ALIGNMENT);
        hudPanel.setBgColor(new Color(38, 8, 55, 177));
        hudPanel.setFontRenderer(mc.fontRenderer);

        HudPanel marketNamePanel = new HudPanel();
        marketNamePanel.add(new HudText(TextFormatting.GOLD + "~gui.hud.text.head.name"));
        marketNamePanel.add(empty);
        marketNamePanel.add(new HudText(String.format("~%s.%s.name", marketTile.getBlockType().getTranslationKey(), marketTile.getMarketBlockType().getName())));

        hudPanel.add(marketNamePanel.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

        switch (marketTile.getMarketBlockType()) {
            case TRADER: {
                HudPanel traderPanelHave = new HudPanel();
                traderPanelHave.add(htlh);
                traderPanelHave.add(empty);
                traderPanelHave.add(hih);
                traderPanelHave.add(empty);
                traderPanelHave.add(htih);

                hudPanel.add(empty);
                hudPanel.add(traderPanelHave.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

                HudPanel traderPanelPrice = new HudPanel();
                traderPanelPrice.add(htlp);
                traderPanelPrice.add(empty);
                traderPanelPrice.add(htp);

                hudPanel.add(empty);
                hudPanel.add(traderPanelPrice.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

                HudPanel traderPanelWant = new HudPanel();
                traderPanelWant.add(htlw);
                traderPanelWant.add(empty);
                traderPanelWant.add(hiw);
                traderPanelWant.add(empty);
                traderPanelWant.add(htiw);

                hudPanel.add(empty);
                hudPanel.add(traderPanelWant.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));
                break;
            }
            case BUYER: {
                HudPanel buyerPanelWant = new HudPanel();
                buyerPanelWant.add(htlw);
                buyerPanelWant.add(empty);
                buyerPanelWant.add(hiw);
                buyerPanelWant.add(empty);
                buyerPanelWant.add(htiw);

                hudPanel.add(empty);
                hudPanel.add(buyerPanelWant.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

                HudPanel buyerPanelPrice = new HudPanel();
                buyerPanelPrice.add(htlp);
                buyerPanelPrice.add(empty);
                buyerPanelPrice.add(htp);

                hudPanel.add(empty);
                hudPanel.add(buyerPanelPrice.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

                break;
            }
            case SELLER: {
                HudPanel sellerPanelHave = new HudPanel();
                sellerPanelHave.add(htlh);
                sellerPanelHave.add(empty);
                sellerPanelHave.add(hih);
                sellerPanelHave.add(empty);
                sellerPanelHave.add(htih);

                hudPanel.add(empty);
                hudPanel.add(sellerPanelHave.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

                HudPanel sellerPanelPrice = new HudPanel();
                sellerPanelPrice.add(htlp);
                sellerPanelPrice.add(empty);
                sellerPanelPrice.add(htp);

                hudPanel.add(empty);
                hudPanel.add(sellerPanelPrice.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));

                break;
            }
        }
        HudPanel ownerPanel = new HudPanel();
        ownerPanel.add(new HudText(TextFormatting.GOLD + "~gui.hud.text.owner.name"));
        ownerPanel.add(empty);
        ownerPanel.add(new HudText(marketTile.getOwner()));

        hudPanel.add(empty);
        hudPanel.add(ownerPanel.setBgColor(new Color(38, 8, 55, 177)).setBgVisible(true));
        draw();
    }

    public enum HudPosType {
        CORNER_BOTTOM_LEFT, CENTER_LEFT, CORNER_TOP_LEFT, CENTER_TOP;
//        CORNER_TOP_RIGHT, CENTER_RIGHT, CORNER_BOTTOM_RIGHT
    }

    private void draw() {
        ScaledResolution scr = new ScaledResolution(mc);
        int w = scr.getScaledWidth();
        int h = scr.getScaledHeight();


        Point hudAnchor = new Point(0,0);
        float scale = .8f;
        int HUD_WIDTH = (int)(hudPanel.getWidth() * scale);
        int HUD_HEIGHT = (int)(hudPanel.getHeight() * scale);
        if (VnjConfig.HUD_OPTIONS.useCustomPos) {
            hudAnchor.x = VnjConfig.HUD_OPTIONS.posX;
            hudAnchor.y = VnjConfig.HUD_OPTIONS.posY;
        }
        else {
            HudPosType hpt = HudPosType.values()[VnjConfig.HUD_OPTIONS.hud_enum_pos];
            if (hpt == HudPosType.CORNER_BOTTOM_LEFT)
                hudAnchor.move(0, h - HUD_HEIGHT - hudPanel.getFontRenderer().FONT_HEIGHT);
            if (hpt == HudPosType.CENTER_LEFT)
                hudAnchor.move(0, h / 2 - HUD_HEIGHT / 2);
            if (hpt == HudPosType.CORNER_TOP_LEFT)
                hudAnchor.move(0, 0);
            if (hpt == HudPosType.CENTER_TOP)
                hudAnchor.move(w / 2 - HUD_WIDTH / 2, 0);
        }
        hudAnchor.translate(2,2);
        hudPanel.setPos((int) (hudAnchor.x / scale), (int) (hudAnchor.y / scale));
        hudPanel.setBgVisible(true);
        hudPanel.setGap(3);
        hudPanel.pack();
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        hudPanel.draw();
        GlStateManager.popMatrix();
    }
}
