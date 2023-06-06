package SuperMario.model.enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class KoopaTroopa extends Enemy {

    private BufferedImage rightImage;
    private BufferedImage shell;
    private boolean isHit;
    private Double lastVelX = 0.0;

    public KoopaTroopa(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48,63);
        setVelX(3);
        isHit = false;
    }

    @Override
    public void draw(Graphics g) {
        if (isHit()) {
            g.drawImage(shell, (int) getX(), (int) getY() + 26, null);
        } else {
            if (getVelX() > 0) {
                g.drawImage(rightImage, (int) getX(), (int) getY(), null);
            } else {
                g.drawImage(getStyle(), (int) getX(), (int) getY(), null);
            }
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
            setX(getX() + 24);
        } else {
            setX(getX() - 24);
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

    public void setShell(BufferedImage shell) {
        this.shell = shell;
    }
}
