package SuperMario.model.enemy.bowser;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Bomb extends GameObject {
    private boolean hasIntersect;
    private boolean exploded;
    private boolean timeToVanish;

    public Bomb(double x, double y, BufferedImage style) {
        super(x, y, style);
        setFalling(false);
        setJumping(true);
        setVelY(15);
    }

    public boolean isTimeToVanish() {
        return timeToVanish;
    }

    public boolean hasIntersect() {
        return hasIntersect;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setTimeToVanish(boolean timeToVanish) {
        this.timeToVanish = timeToVanish;
    }

    public void setHasIntersectFlag(boolean hasIntersect) {
        this.hasIntersect = hasIntersect;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }
}
