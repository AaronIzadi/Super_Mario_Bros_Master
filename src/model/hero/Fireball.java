package model.hero;


import model.GameObj;

import java.awt.image.BufferedImage;

public class Fireball extends GameObj {

    public Fireball(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        setDimension(24, 24);
        setFalling(false);
        setJumping(false);
        setVelX(10);

        if (!toRight){
            setVelX(-5);
        }
    }
}
