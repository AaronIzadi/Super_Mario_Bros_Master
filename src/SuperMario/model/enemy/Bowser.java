package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.model.map.HitPoints;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bowser extends Enemy {

    private int hp;
    private final HitPoints hitPoints;
    private Animation animation;

    public Bowser(double x, double y, BufferedImage style) {
        super(x, y, style);
        hitPoints = HitPoints.getInstance();
        setHp(20);
        setVelX(-1.5);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        animate();
    }

    public void setHp(int hp) {
        this.hp = hp;
        hitPoints.setStyle(hp);
    }

    public int getHp() {
        return hp;
    }

    public void setFrames(BufferedImage[] frames) {
        this.animation = new Animation(frames);
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(7);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
    }
}
