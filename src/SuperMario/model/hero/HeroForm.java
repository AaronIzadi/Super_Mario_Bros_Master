package SuperMario.model.hero;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.model.weapon.Fireball;

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
    private final BufferedImage fireballStyle;

    public HeroForm(BufferedImage[] leftImages, BufferedImage[] rightImages, boolean isSuper, boolean canShootFire, int heroType) {
        this.heroType = heroType;
        this.isSuper = isSuper;
        this.canShootFire = canShootFire;

        setFrames(leftImages, rightImages);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.setHeroType(heroType);
        fireballStyle = imageLoader.getFireballImage();
    }

    public BufferedImage getCurrentStyle(boolean toRight, boolean movingInX, boolean movingInY, boolean isSitting) {

        BufferedImage style;

        if (movingInY) {
            style = toRight ? rightJumpingFrame : leftJumpingFrame;
        } else if (movingInX) {
            Animation currentAnimation = toRight ? rightAnimation : leftAnimation;
            currentAnimation.animate(20);
            style = currentAnimation.getCurrentFrame();
        } else if (isSitting) {
            style = toRight ? rightSittingFrame : leftSittingFrame;
        } else {
            style = toRight ? rightStandingFrame : leftStandingFrame;
        }

        return style;
    }

    private void setFrames(BufferedImage[] leftImages, BufferedImage[] rightImages) {

        int size = leftImages.length;

        leftJumpingFrame = leftImages[0];
        rightJumpingFrame = rightImages[0];
        leftStandingFrame = leftImages[1];
        rightStandingFrame = rightImages[1];
        leftSittingFrame = leftImages[5];
        rightSittingFrame = rightImages[5];

        BufferedImage[] leftFrames = new BufferedImage[size - 3];
        BufferedImage[] rightFrames = new BufferedImage[size - 3];

        for (int i = 0; i < size - 3; i++) {
            leftFrames[i] = leftImages[i + 2];
            rightFrames[i] = rightImages[i + 2];
        }

        rightAnimation = new Animation(rightFrames);
        leftAnimation = new Animation(leftFrames);
    }

    public void onTouchEnemy(ImageLoader imageLoader) {
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(0);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(0);

        setFrames(leftFrames, rightFrames);
    }

    public Fireball fire(boolean toRight, double x, double y) {
        if (canShootFire) {
            return new Fireball(x, y + 48, fireballStyle, toRight);
        }
        return null;
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

    public boolean ifCanShootFire() {
        return canShootFire;
    }

}