package SuperMario.model.hero;

import SuperMario.graphic.manager.Camera;
import SuperMario.logic.GameEngine;


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
        super.setVelYToJump(12);
    }

    @Override
    public void jumpOnEnemy() {
        super.setVelYToJump(6);
    }

    @Override
    public void jumpOnSlime() {
        super.setVelYToJump(18);
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
