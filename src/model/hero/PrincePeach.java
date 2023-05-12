package model.hero;

import graphic.manager.Camera;
import logic.GameEngine;

public class PrincePeach extends Hero{

    public PrincePeach(double x, double y) {
        super(x, y);
        setType(HeroType.PRINCE_PEACH);
    }

    public PrincePeach(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.PRINCE_PEACH);
    }

    @Override
    public void jump() {
        if (!isJumping() && !isFalling()) {
            setJumping(true);
            setVelY(12);
            GameEngine.getInstance().playJump();
        }
    }

    @Override
    public void move(boolean toRight, Camera camera) {
        if (toRight) {
            setVelX(6);
        } else if (camera.getX() < getX()) {
            setVelX(-6);
        }
        setToRight(toRight);
    }

}
