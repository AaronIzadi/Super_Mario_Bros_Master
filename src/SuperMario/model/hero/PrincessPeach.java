package SuperMario.model.hero;

import SuperMario.graphic.manager.Camera;


public class PrincessPeach extends Hero {

    public PrincessPeach(double x, double y) {
        super(x, y);
        setType(HeroType.PRINCESS_PEACH);
    }

    public PrincessPeach(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, width, height, type, heroForm, isSuper, canShootFire);
        setType(HeroType.PRINCESS_PEACH);
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
        if (isSitting()) {
            if (toRight) {
                setVelX(6);
            } else if (camera.getX() < getX()) {
                setVelX(-6);
            }
            setToRight(toRight);
        }
    }

}
