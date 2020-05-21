package vnj.vnjeconomymod.common.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import vnj.vnjeconomymod.client.gui.VnjGuiTextField;
import vnj.vnjeconomymod.common.container.ContainerBase;
import vnj.vnjeconomymod.common.tileEntities.MarketBaseTile;
import vnj.vnjeconomymod.common.tileEntities.MarketTEMethods;
import vnj.vnjeconomymod.network.PacketDispatcher;
import vnj.vnjeconomymod.network.server.MsgUpdatePrice;

import java.io.IOException;

public class GuiContainerBase extends GuiContainer {
    private ResourceLocation background;
    private VnjGuiTextField textFieldTypeIn;
    private GuiButton buttonTextEnter;
    private String textToShow;
    private String invMarketName, invPlayerName;
    private int _xPosT;
    private int _yPosT;
    private int _widthT;
    private int _heightT;
    private int maxChars = 10;
    private int defColor = 0xFFAA00;
    private MarketTEMethods sourceTile;

    public GuiContainerBase(MarketBaseTile tile, EntityPlayer player) {
        super(new ContainerBase(tile, player));
        invPlayerName = player.inventory.getDisplayName().getUnformattedText();
        invMarketName = tile.getMarketBlockType().isBuyer() ? tile.getInventoryOutput().getLocalizedInvName() : tile.getInventoryInput().getLocalizedInvName();
        sourceTile = tile;
        this.xSize = 176;
        this.ySize = sourceTile.getMarketBlockType().isTrader() ? 218 : 178;
        background = sourceTile.getMarketBlockType().getTexture();
    }

    @Override
    public void initGui() {
        super.initGui();
        int charSpacingX = fontRenderer.getCharWidth('_');
        int charSpacingY = fontRenderer.FONT_HEIGHT;

        _widthT = charSpacingX * (maxChars + 1);
        _heightT = charSpacingY;
        _xPosT = guiLeft + (xSize - _widthT) / 2;
        _yPosT = guiTop + 31;

        textToShow = sourceTile.getPrice()+"";

        textFieldTypeIn = new VnjGuiTextField(fontRenderer, _xPosT, _yPosT, _widthT, _heightT);
        textFieldTypeIn.setEnableBackgroundDrawing(false);
        textFieldTypeIn.setMaxStringLength(maxChars);
        textFieldTypeIn.setVisible(true);
        textFieldTypeIn.setFocused(false);
        textFieldTypeIn.setText(textToShow);
        textFieldTypeIn.setTextColor(defColor);

        //int t = Math.round((_xPosT + _widthT) / ContainerBase.SPACING) * ContainerBase.SPACING + ContainerBase.SPACING;
        buttonTextEnter = addButton(new GuiButton(0, _xPosT + _widthT + 5, _yPosT + (_heightT - 10) / 2, 10, 10, ""));
    }

    @Override
    protected void keyTyped(final char character, final int key) throws IOException {
        if(!checkHotbarKeys(key)) {
            if(key == 57 && textFieldTypeIn.getText().isEmpty()) {
                return;
            }

            boolean f = textFieldTypeIn.textboxKeyTyped(character, key);
            if (key == 28) {
                if (isFocused()) {
                    actionPerformed(buttonTextEnter);
                }
            }
            if (!f) {
                super.keyTyped(character, key);
            }
        }
    }
    private void updateTextToShow() {

        String a = textFieldTypeIn.getText();
        if ((a.matches("[0-9]+"))) {
            long l = Long.parseLong(a);
            PacketDispatcher.sendToServer(new MsgUpdatePrice(((MarketBaseTile)sourceTile).getPos(), l));
            textToShow = l + "";
        }
        textFieldTypeIn.setText(textToShow);
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) updateTextToShow();
    }

    public boolean isMouseIn(final int xCoord, final int yCoord) {
        final boolean withinXRange = _xPosT - 2 <= xCoord && xCoord < _xPosT + _widthT + 2;
        final boolean withinYRange = _yPosT - 2 <= yCoord && yCoord < _yPosT + _heightT + 2;

        return withinXRange && withinYRange;
    }
    @Override
    protected void mouseClicked(final int xCoord, final int yCoord, final int btn) throws IOException {
        textFieldTypeIn.mouseClicked(xCoord, yCoord, btn);
        if (isMouseIn(xCoord, yCoord) && isFocused()) {
            textFieldTypeIn.setTextColor(0xFFFFFF);
            if (btn == 1) {
                textFieldTypeIn.setText("");
            }
        } else textFieldTypeIn.setTextColor(defColor);
        super.mouseClicked(xCoord, yCoord, btn);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(invPlayerName, ContainerBase.X_OFFSET, ContainerBase.Y_LOWER_CONTAINER_NAME, 4210752);
        this.fontRenderer.drawString(invMarketName, ContainerBase.X_OFFSET, ContainerBase.Y_UPPER_CONTAINER_NAME, 4210752);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        if (textFieldTypeIn != null) textFieldTypeIn.drawTextBox();
    }
}
