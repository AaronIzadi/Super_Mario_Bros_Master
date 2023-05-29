package SuperMario.model.hero;

import SuperMario.graphic.manager.Camera;
import SuperMario.logic.GameEngine;

public class Luigi extends Hero {
    public Luigi(double x, double y) {
        super(x, y);
        setType(HeroType.LUIGI);
    }

    public Luigi(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.LUIGI);
    }

    @Override
    public void jump() {
        super.setVelYToJump(14);
    }

    @Override
    public void jumpOnEnemy() {
        super.setVelYToJump(7);
    }

    @Override
    public void jumpOnSlime() {
        super.setVelYToJump(21);
    }

    @Override
    public void move(boolean toRight, Camera camera) {
        if (!isSitting()) {
            if (toRight) {
                setVelX(5);
            } else if (camera.getX() < getX()) {
                setVelX(-5);
            }
            setToRight(toRight);
        }
    }
}
