package SuperMario.model.enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Spiny extends Enemy {

    private BufferedImage rightImage;

    public Spiny(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(54, 51);
        setVelX(3);
    }

    public void moveFaster() {
        if (isToRight()) {
            setVelX(6);
        } else {
            setVelX(-6);
        }
    }

    public void moveNormal() {
        if (isToRight()) {
            setVelX(3);
        } else {
            setVelX(-3);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (getVelX() > 0) {
            g.drawImage(rightImage, (int) getX(), (int) getY(), null);
            setToRight(true);
        } else{
            super.draw(g);
            setToRight(false);
        }
    }

    public void setRightImage(BufferedImage rightImage) {
        this.rightImage = rightImage;
    }

}
