package model.enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class KoopaTroopa extends Enemy {

    private BufferedImage rightImage;
    private boolean isHit;
    private Double lastVelX = 0.0;

    public KoopaTroopa(double x, double y, BufferedImage style) {
        super(x, y, style);
        setVelX(3);
        isHit = false;
    }

    @Override
    public void draw(Graphics g) {
        if (getVelX() > 0) {
            g.drawImage(rightImage, (int) getX(), (int) getY(), null);
        } else {
            super.draw(g);
        }
    }

    public void setRightImage(BufferedImage rightImage) {
        this.rightImage = rightImage;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public boolean isHit() {
        return isHit;
    }

    @Override
    public void updateLocation() {
        if (!isHit) {
            super.updateLocation();
            lastVelX = getVelX();
        } else {
            setTimer();
        }
    }

    public void moveAfterHit() {
        if (lastVelX > 0) {
            setX(getX() + 48);
        } else {
            setX(getX() - 48);
        }
    }

    public void setTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setHit(false);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }
}
