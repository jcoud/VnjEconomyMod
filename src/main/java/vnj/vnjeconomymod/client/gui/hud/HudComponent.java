package vnj.vnjeconomymod.client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public abstract class HudComponent {
    public static final int HORIZONTAL_ALIGNMENT = 0;
    public static final int VERTICAL_ALIGNMENT = 1;
    private FontRenderer fontRenderer;
    private Point pos;
    private int width,height;
    private Color bgColor;
    private int alignment;
    private boolean drawBg;
    private int gap;

    HudComponent() {
        alignment = HORIZONTAL_ALIGNMENT;
        drawBg = false;
        bgColor = Color.BLACK;
        pos = new Point(0, 0);
        width = height = gap = 0;
    }

    HudComponent setAlignment(int alignmentId) {
        this.alignment = alignmentId;
        return this;
    }

    int getAlignment() {
        return alignment;
    }

    HudComponent setBgVisible(boolean drawBg) {
        this.drawBg = drawBg;
        return this;
    }

    boolean isBgVisible() {
        return drawBg;
    }

    HudComponent applySettings(HudComponent root) {
        fontRenderer = root.fontRenderer;
        return this;
    }

    HudComponent setBgColor(Color color) {
        this.bgColor = color;
        return this;
    }

    Color getBgColor() {
        return bgColor;
    }

    HudComponent setPos(int x, int y) {
        this.pos.x = x;
        this.pos.y = y;
        return this;
    }

    HudComponent setPos(Point pos) {
        this.pos = pos;
        return this;
    }

    Point getPos() {
        return pos;
    }

    HudComponent setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    HudComponent setGap(int val) {
        pos.translate(val, val);
        this.gap = val;
        return this;
    }

    HudComponent setWidth(int width) {
        this.width = width;
        return this;
    }

    HudComponent setHeight(int height) {
        this.height = height;
        return this;
    }

    FontRenderer getFontRenderer() {
        if (fontRenderer == null) {
            fontRenderer = Minecraft.getMinecraft().fontRenderer;
        }
        return fontRenderer;
    }

    void draw() {
        if (!drawBg) return;
        Gui.drawRect(pos.x - gap, pos.y - gap, pos.x + width + gap, pos.y + height + gap, bgColor.hashCode());
    }

}
