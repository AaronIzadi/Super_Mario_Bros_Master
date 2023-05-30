package SuperMario.model.map;


import SuperMario.input.ImageLoader;

import java.awt.image.BufferedImage;

public class HitPoints {

    private BufferedImage[] frames;
    private BufferedImage style;

    private static final HitPoints instance = new HitPoints();

    private HitPoints() {
        frames = ImageLoader.getInstance().getHitPointFrames();
    }

    public static HitPoints getInstance() {
        return instance;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
    }

    public void setStyle(int hp) {
        if (hp != 0) {
            style = frames[hp - 1];
        } else {
            style = null;
        }
    }

    public BufferedImage getStyle() {
        return style;
    }
}
