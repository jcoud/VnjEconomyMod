package vnj.vnjeconomymod.client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class HudItem extends HudComponent {
    private ItemStack item;

    HudItem(ItemStack itemStack) {
        this.item = itemStack;
//        setWidth(18);
//        setHeight(18);
        setWidth(getFontRenderer().getStringWidth("123"));
        setHeight(getFontRenderer().FONT_HEIGHT);
    }

    @Override
    public void draw() {
        super.draw();

        /*
        *
        * this bit of open-source code under MIT license copied from TheOneProbe 1.12.2
        * link:
        * https://github.com/McJtyMods/TheOneProbe/blob/f4797f1a7f1349ab71ac85e667517117a8a8d51a/src/main/java/mcjty/theoneprobe/rendering/OverlayRenderer.java#L110
        *
        * code is complicated, will be using this until i understand what is what
        *
        * */

        double scale = 1f;
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        double sw = scaledresolution.getScaledWidth_double();
        double sh = scaledresolution.getScaledHeight_double();
        setupOverlayRendering(sw * scale, sh * scale);
        drawItemOnGui();
        setupOverlayRendering(sw, sh);


    }

    private static void setupOverlayRendering(double sw, double sh) {
        GlStateManager.pushMatrix();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sw, sh, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.popMatrix();
    }

    private void drawItemOnGui() {
        if (item == null || item.isEmpty()) return;
        RenderItem ir = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        ir.renderItemAndEffectIntoGUI(item, getPos().x, getPos().y - getFontRenderer().FONT_HEIGHT/2);
        if (item.getCount() > 1)
            ir.renderItemOverlayIntoGUI(getFontRenderer(), item, getPos().x, getPos().y - getFontRenderer().FONT_HEIGHT/2,  item.getCount() + "");
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
}
