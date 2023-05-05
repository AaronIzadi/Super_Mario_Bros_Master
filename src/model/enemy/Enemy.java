package model.enemy;

import model.GameObj;

import java.awt.image.BufferedImage;


public abstract class Enemy extends GameObj {

    public Enemy(double x, double y, BufferedImage style) {
        super(x, y, style);
        setFalling(false);
        setJumping(false);
    }
}
