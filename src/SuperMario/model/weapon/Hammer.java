package SuperMario.model.weapon;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class Hammer extends GameObject {

    private final Hero hero;
    private boolean isReleased = false;
    private BufferedImage leftStyle;
    private final Animation hammerAnimation;
    private double xReleasePoint;
    private boolean gotThere = false;
    private boolean gotBack = false;

    public Hammer(double x, double y, BufferedImage style, Hero hero, Animation hammerAnimation, BufferedImage leftStyle) {
        super(x, y, style);
        this.hero = hero;
        this.hammerAnimation = hammerAnimation;
        this.leftStyle = leftStyle;
        setDimension(68, 68);
        setFalling(false);
        setJumping(false);
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setLeftStyle(BufferedImage leftStyle) {
        this.leftStyle = leftStyle;
    }

    public Hero getHero() {
        return hero;
    }

    public Animation getHammerAnimation() {
        return hammerAnimation;
    }

    public BufferedImage getLeftStyle() {
        return leftStyle;
    }

    public void setReleasedFlag(boolean released) {
        isReleased = released;
    }

    public double getXReleasePoint() {
        return xReleasePoint;
    }

    public void setXReleasePoint(double xReleasePoint) {
        this.xReleasePoint = xReleasePoint;
    }

    public boolean isGotThere() {
        return gotThere;
    }

    public void setGotThere(boolean gotThere) {
        this.gotThere = gotThere;
    }

    public boolean isGotBack() {
        return gotBack;
    }

    public void setGotBack(boolean gotBack) {
        this.gotBack = gotBack;
    }
}
