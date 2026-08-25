package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Piranha extends Enemy {

    private Animation animation;

    public Piranha(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(50, 96);
    }

    public void setFrames(BufferedImage[] frames) {
        setAnimation(new Animation(frames));
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}
