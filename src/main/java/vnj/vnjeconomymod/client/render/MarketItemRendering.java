package vnj.vnjeconomymod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;

public class MarketItemRendering extends TileEntitySpecialRenderer<MarketBaseTile> {

    /* Render method toked from Minecraft@TileEntityEnchantmentTableRenderer */

    @Override
    public void render(MarketBaseTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float xf = (float) x + 0.5F;
        float yf = (float) y + 0.5F;
        float zf = (float) z + 0.5F;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        RenderItem renderItem = mc.getRenderItem();

        short animationTicks = (short) Minecraft.getMinecraft().player.ticksExisted;
        float f1 = MathHelper.sin((animationTicks + partialTicks) / 20.0F) * 0.1F + 0.1F;

//        System.out.printf("f1: %f\r", f1);

        float px = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks);
        float py = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks);
        float pz = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks);

        float tRot;

        ItemStack ihave = te.getDisplayInv().getItemHave();
        ItemStack iwant = te.getDisplayInv().getItemWant();

        GlStateManager.pushMatrix();
        GlStateManager.translate(xf, yf, zf);
        GlStateManager.scale(.6F, .6F, .6F);
        double d0 = px - (double) (te.getPos().getX() + 0.5F);
        double d1 = pz - (double) (te.getPos().getZ() + 0.5F);
        tRot = (float) MathHelper.atan2(d1, d0);

        while (tRot >= (float) Math.PI) tRot -= ((float) Math.PI * 2F);
        while (tRot < -(float) Math.PI) tRot += ((float) Math.PI * 2F);

//        i += 0.1F; if (i == 2F) i = 0F;
//        GlStateManager.shift(0.0F, 0.1F + MathHelper.sin((i + partialTicks) * 0.1F) * 0.01F, 0.0F);
        GlStateManager.translate(0.0F, f1, 0.0F);
        GlStateManager.rotate(-tRot * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);

        if (te.getDistanceSq(px, py, pz) < 128d) {
            if (!ihave.isEmpty() && !iwant.isEmpty()) {
                renderOne(renderItem, iwant, 0.4F);
                renderOne(renderItem, ihave, -0.4F);
            } else {
                if (!ihave.isEmpty())
                    renderOne(renderItem, ihave, 0F);
                else
                if (!iwant.isEmpty())
                    renderOne(renderItem, iwant, 0F);
            }
        }
        GlStateManager.popMatrix();
    }

    private void renderOne(RenderItem renderItem, ItemStack item, float shiftX) {
        RenderHelper.enableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate(shiftX, 0, 0);
        GlStateManager.rotate(180, 0F, 1F, 0F);
        GlStateManager.disableLighting();
        if (!(item.getItem() instanceof ItemBlock)) GlStateManager.scale(0.7F, 0.7F, 0.7F);
        GlStateManager.enableTexture2D();
        renderItem.renderItem(item, ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.rotate(-180, 0F, 1F, 0F);
        GlStateManager.popMatrix();
    }
}
