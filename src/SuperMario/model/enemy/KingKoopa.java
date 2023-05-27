package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class KingKoopa extends Enemy{

    private int hp;
    private Animation animation;
    private BufferedImage fireStyle;

    public KingKoopa(double x, double y, BufferedImage style) {
        super(x, y, style);
        setHp(20);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        animate();
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setFrames(BufferedImage[] frames) {
        this.animation = new Animation(frames);
    }

    public void setFireStyle(BufferedImage fireStyle) {
        this.fireStyle = fireStyle;
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(5);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
    }
}
