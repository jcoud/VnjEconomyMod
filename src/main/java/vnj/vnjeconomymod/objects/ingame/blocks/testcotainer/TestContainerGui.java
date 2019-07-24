package vnj.vnjeconomymod.objects.ingame.blocks.testcotainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import vnj.vnjeconomymod.VnjEconomyMod;

public class TestContainerGui extends GuiContainer {
    private IInventory lowerContainerPos;
    private IInventory upperContainerPos;
    private static final ResourceLocation background = new ResourceLocation(VnjEconomyMod.MOD_ID, "textures/gui/testcontainer.png");

    public TestContainerGui(TestContainerTileEntity tileEntity, TestContainer container) {
        super(container);
        this.lowerContainerPos = container.getPlayerInventory();
        this.upperContainerPos = container.getSelfInventory();
        xSize = 180;
        ySize = TestContainer.yOffsetMax; // 152
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    private void drawBox(){
        this.drawVerticalLine(0, 0, ySize, Integer.parseInt("2BFF00", 16));
        this.drawVerticalLine(xSize, 0, ySize, Integer.parseInt("0011FF", 16));
        this.drawHorizontalLine(0, xSize, 0, Integer.parseInt("EFFF00", 16));
        this.drawHorizontalLine(0, xSize, ySize, Integer.parseInt("FF0000", 16));
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawBox();
        this.fontRenderer.drawString(this.lowerContainerPos.getDisplayName().getUnformattedText(), TestContainer.xOffset, TestContainer.yOffset * 2, 4210752);
        this.fontRenderer.drawString(this.upperContainerPos.getDisplayName().getUnformattedText(), TestContainer.xOffset, TestContainer.yOffset, 4210752);
    }



    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
