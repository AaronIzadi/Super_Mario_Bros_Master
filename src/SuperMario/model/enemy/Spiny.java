package SuperMario.model.enemy;

import SuperMario.logic.enemy.EnemyLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Spiny extends Enemy {

    private BufferedImage rightImage;

    public Spiny(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(54, 51);
        setVelX(3);
    }

    @Override
    public void draw(Graphics g) {
        EnemyLogic.draw(this, g);
    }

    @Override
    public void updateLocation() {
        EnemyLogic.update(this);
    }

    public void moveFaster() {
        EnemyLogic.moveFaster(this);
    }

    public void moveNormal() {
        EnemyLogic.moveNormal(this);
    }

    public void setRightImage(BufferedImage rightImage) {
        this.rightImage = rightImage;
    }

    public BufferedImage getRightImage() {
        return rightImage;
    }
}
