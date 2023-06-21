package SuperMario.model.weapon;


import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Fireball extends GameObject {

    public Fireball(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        setDimension(24, 24);
        setFalling(false);
        setJumping(false);
        setToRight(toRight);
        setVelX(10);

        if (!toRight) {
            setVelX(-10);
        }
    }
}
