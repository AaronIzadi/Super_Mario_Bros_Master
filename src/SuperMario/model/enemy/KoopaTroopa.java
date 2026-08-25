package SuperMario.model.enemy;

import SuperMario.logic.enemy.EnemyLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        EnemyLogic.draw(this, g);
    }

    @Override
    public void updateLocation() {
        EnemyLogic.update(this);
    }

    public void moveAfterHit() {
        EnemyLogic.moveAfterHit(this);
    }

    public void setTimer() {
        EnemyLogic.setTimer(this);
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

    public void setShell(BufferedImage shell) {
        this.shell = shell;
    }

    public BufferedImage getRightImage() {
        return rightImage;
    }

    public BufferedImage getShell() {
        return shell;
    }

    public Double getLastVelX() {
        return lastVelX;
    }

    public void setLastVelX(Double lastVelX) {
        this.lastVelX = lastVelX;
    }
}
