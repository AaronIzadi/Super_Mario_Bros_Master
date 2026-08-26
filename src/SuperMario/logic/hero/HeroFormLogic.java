package SuperMario.logic.hero;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.model.hero.HeroForm;
import SuperMario.model.weapon.Fireball;

import java.awt.image.BufferedImage;

public final class HeroFormLogic {

    private HeroFormLogic() {
    }

    public static HeroForm createForm(BufferedImage[] leftImages, BufferedImage[] rightImages,
                                      boolean isSuper, boolean canShootFire, int heroType) {
        HeroForm form = new HeroForm(leftImages, rightImages, isSuper, canShootFire, heroType);
        configureFrames(form, leftImages, rightImages);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.setHeroType(heroType);
        form.setFireballStyle(imageLoader.getFireballImage());
        return form;
    }

    public static BufferedImage getCurrentStyle(HeroForm form, boolean toRight, boolean movingInX,
                                                boolean movingInY, boolean isCrouching) {
        BufferedImage style;

        if (movingInY) {
            style = toRight ? form.getRightJumpingFrame() : form.getLeftJumpingFrame();
        } else if (movingInX) {
            Animation currentAnimation = toRight ? form.getRightAnimation() : form.getLeftAnimation();
            style = currentAnimation.getCurrentFrame();
        } else if (isCrouching) {
            style = toRight ? form.getRightSittingFrame() : form.getLeftSittingFrame();
        } else {
            style = toRight ? form.getRightStandingFrame() : form.getLeftStandingFrame();
        }

        return style;
    }

    public static void animateWalking(HeroForm form, boolean toRight) {
        Animation animation = toRight ? form.getRightAnimation() : form.getLeftAnimation();
        if (animation != null) {
            animation.animate(20);
        }
    }

    public static void resetToSmallOnDamage(HeroForm form, ImageLoader imageLoader) {
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(0);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(0);
        configureFrames(form, leftFrames, rightFrames);
    }

    public static Fireball createFireball(HeroForm form, boolean toRight, double x, double y, int heroHeight) {
        if (form.canShootFire()) {
            double fireballY = y + heroHeight / 2.0 - 12;
            return new Fireball(x, fireballY, form.getFireballStyle(), toRight);
        }
        return null;
    }

    public static void configureFrames(HeroForm form, BufferedImage[] leftImages, BufferedImage[] rightImages) {
        int size = leftImages.length;

        form.setLeftJumpingFrame(leftImages[0]);
        form.setRightJumpingFrame(rightImages[0]);
        form.setLeftStandingFrame(leftImages[1]);
        form.setRightStandingFrame(rightImages[1]);
        form.setLeftSittingFrame(leftImages[5]);
        form.setRightSittingFrame(rightImages[5]);

        BufferedImage[] leftFrames = new BufferedImage[size - 3];
        BufferedImage[] rightFrames = new BufferedImage[size - 3];

        for (int i = 0; i < size - 3; i++) {
            leftFrames[i] = leftImages[i + 2];
            rightFrames[i] = rightImages[i + 2];
        }

        form.setRightAnimation(new Animation(rightFrames));
        form.setLeftAnimation(new Animation(leftFrames));
    }
}
