package SuperMario.model.hero;


import SuperMario.graphic.manager.Camera;
import SuperMario.logic.GameEngine;

public class Mario extends Hero{

    public Mario(double x, double y) {
        super(x, y);
        setType(HeroType.MARIO);
    }

    public Mario(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.MARIO);
    }

    @Override
    public void jump() {
        super.setVelYToJump(10);
    }

    @Override
    public void jumpOnEnemy() {
        super.setVelYToJump(5);

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
}
