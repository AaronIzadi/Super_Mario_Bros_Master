package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.brick.BrickLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LavaBorder extends Border {

    private Animation animation;
    private boolean burn;
    private final BufferedImage mainStyle;

    public LavaBorder(double x, double y, BufferedImage style) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
        setDimension(48, 48);
        burn = false;
        this.mainStyle = style;
    }

    @Override
    public void draw(Graphics g) {
        BrickLogic.draw(this, g);
    }

    public void setBurn(boolean burn) {
        this.burn = burn;
    }

    public void setFrames(BufferedImage[] frames) {
        animation = new Animation(frames);
    }

    public void animate() {
        BrickLogic.animate(this);
    }

    public boolean isBurn() {
        return burn;
    }

    public Animation getAnimation() {
        return animation;
    }

    public BufferedImage getMainStyle() {
        return mainStyle;
    }
}
