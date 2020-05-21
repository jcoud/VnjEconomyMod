package vnj.vnjeconomymod.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class VnjGuiTextField extends GuiTextField {
    private int x;
    private int y;
    private int width;
    private int height;
    private static final int PADDING = 2;
    public static int getPadding() {return VnjGuiTextField.PADDING;}

    public VnjGuiTextField(FontRenderer fontrendererObj, int x, int y, int width, int height) {
        super(0, fontrendererObj, x + PADDING, y + PADDING, width - PADDING * 2, height - PADDING * 2);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawTextBox() {
        int colori = this.isFocused() ? 0xFF606060 : 0xFFA8A8A8;
        drawRect(this.x - PADDING + 1, this.y - PADDING + 1, this.x + this.width + PADDING - 1, this.y + this.height + PADDING * 2 - 1, colori);
        super.drawTextBox();

    }
}
