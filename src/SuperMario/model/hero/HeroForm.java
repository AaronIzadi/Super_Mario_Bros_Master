package SuperMario.model.hero;

import SuperMario.graphic.view.animation.Animation;

import java.awt.image.BufferedImage;

public class HeroForm {

    public static final int SMALL = 0;
    public static final int SUPER = 1;
    public static final int FIRE = 2;
    private int heroType;
    private Animation leftAnimation;
    private Animation rightAnimation;
    private BufferedImage leftStandingFrame;
    private BufferedImage rightStandingFrame;
    private BufferedImage leftJumpingFrame;
    private BufferedImage rightJumpingFrame;
    private BufferedImage leftSittingFrame;
    private BufferedImage rightSittingFrame;
    private boolean isSuper;
    private boolean canShootFire;
    private BufferedImage fireballStyle;

    public HeroForm(BufferedImage[] leftImages, BufferedImage[] rightImages, boolean isSuper, boolean canShootFire, int heroType) {
        this.heroType = heroType;
        this.isSuper = isSuper;
        this.canShootFire = canShootFire;
    }

    public int getHeroType() {
        return heroType;
    }

    public void setHeroType(int heroType) {
        this.heroType = heroType;
    }

    public void setCanShootFire(boolean canShootFire) {
        this.canShootFire = canShootFire;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    public boolean canShootFire() {
        return canShootFire;
    }

    public Animation getLeftAnimation() {
        return leftAnimation;
    }

    public void setLeftAnimation(Animation leftAnimation) {
        this.leftAnimation = leftAnimation;
    }

    public Animation getRightAnimation() {
        return rightAnimation;
    }

    public void setRightAnimation(Animation rightAnimation) {
        this.rightAnimation = rightAnimation;
    }

    public BufferedImage getLeftStandingFrame() {
        return leftStandingFrame;
    }

    public void setLeftStandingFrame(BufferedImage leftStandingFrame) {
        this.leftStandingFrame = leftStandingFrame;
    }

    public BufferedImage getRightStandingFrame() {
        return rightStandingFrame;
    }

    public void setRightStandingFrame(BufferedImage rightStandingFrame) {
        this.rightStandingFrame = rightStandingFrame;
    }

    public BufferedImage getLeftJumpingFrame() {
        return leftJumpingFrame;
    }

    public void setLeftJumpingFrame(BufferedImage leftJumpingFrame) {
        this.leftJumpingFrame = leftJumpingFrame;
    }

    public BufferedImage getRightJumpingFrame() {
        return rightJumpingFrame;
    }

    public void setRightJumpingFrame(BufferedImage rightJumpingFrame) {
        this.rightJumpingFrame = rightJumpingFrame;
    }

    public BufferedImage getLeftSittingFrame() {
        return leftSittingFrame;
    }

    public void setLeftSittingFrame(BufferedImage leftSittingFrame) {
        this.leftSittingFrame = leftSittingFrame;
    }

    public BufferedImage getRightSittingFrame() {
        return rightSittingFrame;
    }

    public void setRightSittingFrame(BufferedImage rightSittingFrame) {
        this.rightSittingFrame = rightSittingFrame;
    }

    public BufferedImage getFireballStyle() {
        return fireballStyle;
    }

    public void setFireballStyle(BufferedImage fireballStyle) {
        this.fireballStyle = fireballStyle;
    }
}
