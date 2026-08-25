package SuperMario.model.enemy;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Spiny extends Enemy {

    private BufferedImage rightImage;

    public Spiny(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(54, 51);
        setVelX(3);
    }

    public void setRightImage(BufferedImage rightImage) {
        this.rightImage = rightImage;
    }

    public BufferedImage getRightImage() {
        return rightImage;
    }
}
