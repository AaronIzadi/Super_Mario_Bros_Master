package SuperMario.model.enemy.bowser;

import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.model.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameObject {
    private boolean hasIntersect;
    private boolean exploded;
    private boolean timeToVanish;

    public Bomb(double x, double y, BufferedImage style) {
        super(x, y, style);
        setFalling(false);
        setJumping(true);
        setVelY(15);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if (hasIntersect) {
            setStyle(ImageLoader.getInstance().getBombOn());
            setTimerToExplode();
        }
        if (exploded) {
            setTimerToVanish();
        }
    }

    public void setHasIntersect(boolean hasIntersect) {
        this.hasIntersect = hasIntersect;
    }

    public void setTimerToExplode() {
        hasIntersect = false;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setStyle(ImageLoader.getInstance().getBombExplode());
                GameEngine.getInstance().playBowserFireBall();
                exploded = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2500);
    }

    public void setTimerToVanish() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setStyle(null);
                timeToVanish = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1500);
    }

    public boolean isTimeToVanish() {
        return timeToVanish;
    }

    public boolean hasIntersect() {
        return hasIntersect;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setTimeToVanish(boolean timeToVanish) {
        this.timeToVanish = timeToVanish;
    }
}
