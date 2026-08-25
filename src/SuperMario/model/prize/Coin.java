package SuperMario.model.prize;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Coin extends GameObject implements Prize {

    private final int point;
    private boolean revealed;
    private boolean acquired = false;
    private final int revealBoundary;

    public Coin(double x, double y, BufferedImage style, int point) {
        super(x, y, style);
        this.point = point;
        revealed = false;
        setDimension(30, 42);
        revealBoundary = (int) getY() - getDimension().height;
    }

    @Override
    public int getPoint() {
        return point;
    }

    public int getRevealBoundary() {
        return revealBoundary;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }
}
