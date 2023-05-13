package SuperMario.model.hero;


import SuperMario.graphic.manager.Camera;
import SuperMario.logic.GameEngine;

public class Ross extends Hero{
    public Ross(double x, double y) {
        super(x, y);
        setType(HeroType.ROSS);
    }

    public Ross(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.ROSS);
    }

    @Override
    public void jump() {
        if (!isJumping() && !isFalling()) {
            setJumping(true);
            setVelY(10);
            GameEngine.getInstance().playJump();
        }
    }

    @Override
    public void move(boolean toRight, Camera camera) {
        if (toRight) {
            setVelX(7);
        } else if (camera.getX() < getX()) {
            setVelX(-7);
        }
        setToRight(toRight);
    }
}
