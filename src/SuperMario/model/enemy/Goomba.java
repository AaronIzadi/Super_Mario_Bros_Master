package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Goomba extends Enemy {

    private Animation animation;

    public Goomba(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
        setVelX(3);
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
