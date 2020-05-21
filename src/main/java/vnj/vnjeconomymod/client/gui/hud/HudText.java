package vnj.vnjeconomymod.client.gui.hud;

import net.minecraft.client.resources.I18n;

class HudText extends HudComponent {
    private String text;
    private static final String FORMAT_KEY = "~";

    HudText(String text) {
        this.text = text;
        if (text.length() > 1) {
            if (text.substring(0, 1).equals(FORMAT_KEY)) {
                this.text = I18n.format(text.substring(1));
            }
            else if (text.substring(0, 1).equals("\u00a7") && text.substring(2, 3).equals(FORMAT_KEY)) {
                this.text = text.substring(0, 2) + I18n.format(text.substring(3));
            }
        }
        else setWidth(getFontRenderer().getStringWidth("_"));
        setWidth(getFontRenderer().getStringWidth(this.text));
        setHeight(getFontRenderer().FONT_HEIGHT);
    }

    @Override
    public void draw() {
        super.draw();
        drawString();
    }

    private void drawString() {
        if (text == null || text.equals("")) return;
        getFontRenderer().drawString(text, getPos().x, getPos().y, 16777215, false);
    }
}
