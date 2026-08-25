package SuperMario.model.obstacle;

import java.awt.image.BufferedImage;

public class Slime extends Brick {
    private BufferedImage slimeOnTouch;
    private boolean onTouch;

    public Slime(double x, double y, BufferedImage style) {
        super(x + 4, y, style);
        setBreakable(true);
        setEmpty(true);
    }

    public void slimeOnTouch(BufferedImage slimeOnTouch) {
        this.slimeOnTouch = slimeOnTouch;
    }

    public boolean isOnTouch() {
        return onTouch;
    }

    public void setOnTouchFlag(boolean onTouch) {
        this.onTouch = onTouch;
    }

    public BufferedImage getSlimeOnTouch() {
        return slimeOnTouch;
    }
}
