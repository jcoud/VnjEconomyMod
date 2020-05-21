package vnj.vnjeconomymod.client.gui.hud;

public class HudEmpty extends HudComponent {
    public HudEmpty() {
        setWidth(getFontRenderer().getStringWidth("_"));
        setHeight(getFontRenderer().FONT_HEIGHT);
    }
}
