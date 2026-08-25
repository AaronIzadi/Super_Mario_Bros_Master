package SuperMario.model.prize;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class PrizeItems extends GameObject implements Prize {

    private final PrizeType type;
    private boolean revealed = false;
    private int point;

    public PrizeItems(double x, double y, BufferedImage style, PrizeType type) {
        super(x, y, style);
        this.type = type;
        this.point = type.getPoints();
        setDimension(48, 48);
    }

    public PrizeType getType() {
        return type;
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
