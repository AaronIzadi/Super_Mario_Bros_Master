package SuperMario.model.enemy;

import SuperMario.graphic.view.animation.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Piranha extends Enemy {

    private Animation animation;

    public Piranha(double x, double y, BufferedImage style) {
        super(x + 22, y, style);
    }

    @Override
    public void draw(Graphics g) {
        if (getY() >= 580) {
            setY(580);
            setVelY(0);
            setTimerToGoUp();
        }
        if (getY() <= 480) {
            setY(480);
            setVelY(0);
            setTimerToGoDown();
        }
        super.draw(g);
        animate();
    }

    public void setTimerToGoDown() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setVelY(-1);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }

    public void setTimerToGoUp() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setVelY(1);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
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

    @Override
    public void updateLocation() {
        super.updateLocation();
    }
}
