package SuperMario.model.enemy;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class KoopaTroopa extends Enemy {

    private BufferedImage rightImage;
    private BufferedImage shell;
    private boolean isHit;
    private Double lastVelX = 0.0;

    public KoopaTroopa(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 63);
        setVelX(3);
        isHit = false;
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
