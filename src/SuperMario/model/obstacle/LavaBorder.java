package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LavaBorder extends Brick {

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
        if (burn) {
            animate();
        } else {
            setStyle(mainStyle);
        }
        super.draw(g);
    }

    public void setBurn(boolean burn) {
        this.burn = burn;
    }

    public void setFrames(BufferedImage[] frames) {
        animation = new Animation(frames);
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(8);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
    }
}
