package vnj.vnjeconomymod.client.gui.hud;

import java.awt.*;
import java.util.ArrayList;

class HudPanel extends HudComponent {
    private ArrayList<HudComponent> labelList;

    HudPanel(int alignment) {
        setAlignment(alignment);
        labelList = new ArrayList<>();
    }
    HudPanel() {
        labelList = new ArrayList<>();
    }

    void add(HudComponent component) {
        component.applySettings(this);
        if (getAlignment() == HudComponent.HORIZONTAL_ALIGNMENT) {
            component.setPos(getWidth(), getPos().y+1);
            setWidth(Math.max(getWidth(), getWidth() + component.getWidth()));
            setHeight(Math.max(getHeight(), component.getHeight()));
        }
        else {
            component.setPos(getPos().x, getHeight()+1);
            setWidth(Math.max(getWidth(), component.getWidth()));
            setHeight(Math.max(getHeight(), getHeight() + component.getHeight()));
        }

        labelList.add(component);
    }

    private ArrayList<HudComponent> getComponents() {
        return labelList;
    }

    void pack() {
        updateChildren(this);
    }

    private void updateChildren(HudPanel ac) {
        for (HudComponent h : ac.getComponents()) {
            Point p = h.getPos();
            p.translate(ac.getPos().x, ac.getPos().y);
            h.setPos(p);
            if (h instanceof HudPanel) {
                updateChildren((HudPanel) h);
            }
        }
    }

    @Override
    public void draw() {
        super.draw();
        for (HudComponent hudComponent : labelList) hudComponent.draw();
    }
}
