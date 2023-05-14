package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Piranha extends Enemy {

    private Animation animation;

    public Piranha(double x, double y, BufferedImage style) {
        super(x, y, style);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        animate();
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(7);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
    }

    public void setFrames(BufferedImage[] frames) {
        setAnimation(new Animation(frames));
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
