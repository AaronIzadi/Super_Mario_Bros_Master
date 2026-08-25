package SuperMario.model.prize;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public abstract class PrizeItems extends GameObject implements Prize {

    private boolean revealed = false;
    private int point;

    public PrizeItems(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    @Override
    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
