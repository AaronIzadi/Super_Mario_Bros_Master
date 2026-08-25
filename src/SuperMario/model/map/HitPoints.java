package SuperMario.model.map;

import java.awt.image.BufferedImage;

public class HitPoints {

    private BufferedImage[] frames;
    private BufferedImage style;

    public HitPoints() {
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
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
