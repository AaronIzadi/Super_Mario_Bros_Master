package SuperMario.model.map;

import SuperMario.logic.map.BossHudLogic;

import java.awt.image.BufferedImage;

public class HitPoints {

    private BufferedImage[] frames;
    private BufferedImage style;

    private static final HitPoints instance = new HitPoints();

    private HitPoints() {
    }

    public static HitPoints getInstance() {
        return instance;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
    }

    public void setStyle(int hp) {
        BossHudLogic.setStyle(this, hp);
    }

    public BufferedImage getStyle() {
        return style;
    }

    public BufferedImage[] getFrames() {
        return frames;
    }

    public void setStyleImage(BufferedImage style) {
        this.style = style;
    }
}
