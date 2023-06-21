package SuperMario.model.enemy.bowser;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public class Fire extends GameObject {

    public Fire(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        setDimension(96, 47);
        setFalling(false);
        setJumping(false);
        setVelX(7);

        if (!toRight) {
            setVelX(-7);
        }
    }
}
