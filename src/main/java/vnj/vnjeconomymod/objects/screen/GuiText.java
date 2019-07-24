package vnj.vnjeconomymod.objects.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class GuiText extends Gui {
    public GuiText(Minecraft mc){
        ScaledResolution scr = new ScaledResolution(mc);
        int w = scr.getScaledWidth();
        int h = scr.getScaledHeight();
        String s = "test";
        drawCenteredString(mc.fontRenderer, s, w/2 + w/4 + mc.fontRenderer.getStringWidth(s), h - 8, Integer.parseInt("FFAA00", 16));
    }
}
