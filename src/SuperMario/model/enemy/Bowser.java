package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.model.map.InvisiblePoint;
import SuperMario.model.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bowser extends Enemy {

    private int hp;
    private Animation animation;
    private BufferedImage fireStyle;
    private InvisiblePoint x1;
    private InvisiblePoint x2;

    public Bowser(double x, double y, BufferedImage style) {
        super(x, y, style);
        setHp(20);
        setVelX(-0.5);
    }

    @Override
    public void draw(Graphics g) {
        if (getVelX() > 0) {
            if (Math.floor(getX()) == x2.getX()) {
                setVelX(-0.5);
            }
        } else {
            if (Math.floor(getX()) == x1.getX()) {
                setVelX(0.5);
            }
        }
        super.draw(g);
        animate();
    }

    public void setBounds(InvisiblePoint x1, InvisiblePoint x2) {
        this.x1 = x1;
        this.x2 = x2;
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
