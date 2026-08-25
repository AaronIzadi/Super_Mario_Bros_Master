package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.enemy.EnemyLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Piranha extends Enemy {

    private Animation animation;

    public Piranha(double x, double y, BufferedImage style) {
        super(x , y, style);
        setDimension(50,96);
    }

    @Override
    public void draw(Graphics g) {
        EnemyLogic.draw(this, g);
    }

    @Override
    public void updateLocation() {
        EnemyLogic.update(this);
    }

    public void setTimerToGoDown() {
        EnemyLogic.setTimerToGoDown(this);
    }

    public void setTimerToGoUp() {
        EnemyLogic.setTimerToGoUp(this);
    }

    public void animate() {
        EnemyLogic.animate(this);
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
