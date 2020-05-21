package vnj.vnjeconomymod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vnj.vnjeconomymod.data.UserInternal;

public class GuiScreenText extends Gui {
    public GuiScreenText(Minecraft mc){
        ScaledResolution scr = new ScaledResolution(mc);
        int w = scr.getScaledWidth();
        int h = scr.getScaledHeight();
        String s = "test " + UserInternal.balance;
        int fh = mc.fontRenderer.FONT_HEIGHT + 2;
        int fw = mc.fontRenderer.getStringWidth(s);
        drawCenteredString(mc.fontRenderer, s, w/2 + w/4 + fw, h - fh, 0xFFFFAA00);
        //GlStateManager.color(1F, 1F, 1F, 1F);
    }
}
