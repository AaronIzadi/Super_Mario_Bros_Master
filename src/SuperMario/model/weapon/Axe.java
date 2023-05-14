package SuperMario.model.weapon;


import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Axe extends GameObject {

    private final Hero hero;
    private boolean isReleased;
    private BufferedImage leftStyle;
    private BufferedImage[] axeStyle;
    private final Animation axeAnimation;

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
        if (hero.getToRight()) {
            g.drawImage(getStyle(), (int) getX(), (int) getY(), null);
        } else {
            g.drawImage(leftStyle, (int) getX(), (int) getY(), null);
        }
    }

    public BufferedImage[] getAxeStyle() {
        return axeStyle;
    }

    public void setAxeStyle(BufferedImage[] axeStyle) {
        this.axeStyle = axeStyle;
    }

    public void setReleased(boolean released) {
        axeAnimation.animate(20);
        isReleased = released;
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setLeftStyle(BufferedImage leftStyle) {
        this.leftStyle = leftStyle;
    }
}
