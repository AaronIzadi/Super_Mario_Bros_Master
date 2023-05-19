package SuperMario.model.hero;

import SuperMario.graphic.manager.Camera;
import SuperMario.logic.GameEngine;


public class Toad extends Hero {
    public Toad(double x, double y) {
        super(x, y);
        setType(HeroType.TOAD);
    }

    public Toad(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.TOAD);
        setRemainingLives(5);
    }

    @Override
    public void jump() {
        super.setVelYToJump(10);
    }

    @Override
    public void jumpOnEnemy() {
        super.setVelYToJump(15);
    }

    @Override
    public void jumpOnSlime() {
        super.setVelYToJump(15);
    }

    @Override
    public void move(boolean toRight, Camera camera) {
        if (toRight) {
            setVelX(5);
        } else if (camera.getX() < getX()) {
            setVelX(-5);
        }
        setToRight(toRight);
    }

    @Override
    public void acquireCoin() {
        super.acquireCoin();
        setExtraCoin();
    }

    public void setExtraCoin() {
        setCoins(getCoins() + 1);
    }
}
