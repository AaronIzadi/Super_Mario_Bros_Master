package SuperMario.model.weapon;


import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Axe extends GameObject {

    private final Hero hero;
    private boolean isReleased = false;
    private BufferedImage leftStyle;
    private final Animation axeAnimation;
    private double xReleasePoint;
    private boolean gotThere = false;
    private boolean gotBack = false;


    public Axe(double x, double y, BufferedImage style, Hero hero) {
        super(x, y, style);
        this.hero = hero;
        this.axeAnimation = new Animation(ImageLoader.getInstance().axeFrames());
        setDimension(68, 68);
        setFalling(false);
        setJumping(false);
        setLeftStyle(ImageLoader.getInstance().getAxeUpLeft());
    }

    @Override
    public void draw(Graphics g) {
        if (!isReleased) {
            if (hero.getToRight()) {
                g.drawImage(getStyle(), (int) getX(), (int) getY(), null);
            } else {
                g.drawImage(leftStyle, (int) getX(), (int) getY(), null);
            }
        } else {
            super.draw(g);
            animate();
        }
    }

    public void animate() {
        boolean isAnimationTicked = axeAnimation.animate(25);
        if (isAnimationTicked) {
            setStyle(axeAnimation.getCurrentFrame());
        }
    }

    public void setReleased(boolean released, double xReleasePoint) {
        this.isReleased = released;
        this.xReleasePoint = xReleasePoint;
        if (isReleased) {
            setVelX(8);
            if (!hero.getToRight()) {
                setVelX(-8);
            }
        }
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setLeftStyle(BufferedImage leftStyle) {
        this.leftStyle = leftStyle;
    }

    @Override
    public void updateLocation() {
        if (gotThere) {
            double dx = hero.getX() - getX();
            double dy = (-1) * ((hero.getY() + 48) - getY());
            double time = 4;

            setVelX(dx / time);
            setVelY(dy / time);
        }

        setY(getY() - getVelY());
        setX(getX() + getVelX());

        if (Math.abs(xReleasePoint - getX()) >= (4 * 48)) {
            gotThere = true;
        }

        if (Math.floor(hero.getX()) == Math.floor(getX()) || Math.ceil(hero.getX()) == Math.ceil(getX())) {
            gotBack = true;
        }

        if (gotThere && gotBack) {
            hero.deactivateAxe();
        }
    }
}
