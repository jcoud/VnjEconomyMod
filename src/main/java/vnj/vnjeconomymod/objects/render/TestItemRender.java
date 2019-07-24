package vnj.vnjeconomymod.objects.render;

import com.google.common.primitives.SignedBytes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import vnj.vnjeconomymod.objects.ingame.blocks.testcotainer.TestContainerTileEntity;

public class TestItemRender extends TileEntitySpecialRenderer<TestContainerTileEntity> {

    private EntityItem entityItem;
    private RenderEntityItem renderEntityItem;

    @Override
    public void render(TestContainerTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        entityItem = new EntityItem(getWorld(), x, y, z);
        ItemStack item = te.getFirstItem();
        if (item.isEmpty()) {
            return;
        }
        float timeD = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;

        entityItem.hoverStart = 0F;
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        GlStateManager.translate((float) x + 0.5F, (float) y + 1F, (float) z + 0.5F);
        GlStateManager.rotate(timeD, 0F, 1F, 0F);
        GlStateManager.scale(0.8F, 0.8F, 0.8F);
        //GlStateManager.translate(0.5F, 0.5F, 0.5F);

        entityItem.setItem(item);
        if (this.renderEntityItem == null) {
            this.renderEntityItem = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
                @Override
                public int getModelCount(ItemStack stack) {
                    return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
                }
                @Override
                public boolean shouldBob() {
                    return false;
                }

                @Override
                public boolean shouldSpreadItems() {
                    return true;
                }
            };
        }

        this.renderEntityItem.doRender(entityItem, 0D, 0D, 0D, 0F, partialTicks);

        GlStateManager.popMatrix();
    }
}
