package SuperMario.logic.prize;

import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroForm;
import SuperMario.model.prize.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public final class PrizeLogic {

    private PrizeLogic() {
    }

    public static void onTouch(Coin coin, Hero hero, GameEngine engine) {
        if (!coin.isAcquired()) {
            coin.setAcquired(true);
            hero.acquirePoints(coin.getPoint());
            hero.acquireCoin();
            engine.getSoundManager().playCoin();
        }
    }

    public static void reveal(Coin coin) {
        coin.setRevealed(true);
    }

    public static void update(Coin coin) {
        if (coin.isRevealed()) {
            coin.setY(coin.getY() - 5);
        }
    }

    public static void draw(Coin coin, Graphics g) {
        if (coin.isRevealed()) {
            EntityRenderer.draw(coin, g);
        }
    }

    public static void onTouch(PrizeItems prize, Hero hero, GameEngine engine) {
        hero.acquirePoints(prize.getPoint());

        if (!hero.isSuper()) {
            hero.setY(hero.getBottomBounds().getY() - hero.getDimension().getHeight());
            hero.setDimension(48, 96);
        }
        if (!hero.getHeroForm().isSuper()) {
            setHeroForm(hero, HeroForm.SUPER);
        } else {
            setHeroForm(hero, HeroForm.FIRE);
        }

        engine.getSoundManager().playPowerUp();
    }

    public static void onTouch(SuperMushroom mushroom, Hero hero, GameEngine engine) {
        onTouch((PrizeItems) mushroom, hero, engine);
        engine.getSoundManager().playPowerUp();
    }

    public static void onTouch(FireFlower flower, Hero hero, GameEngine engine) {
        onTouch((PrizeItems) flower, hero, engine);
        engine.getSoundManager().playPowerUp();
    }

    public static void onTouch(HeartMushroom heart, Hero hero, GameEngine engine) {
        hero.acquirePoints(heart.getPoint());
        hero.setRemainingLives(hero.getRemainingLives() + 1);
        engine.getSoundManager().playOneUp();
    }

    public static void onTouch(SuperStar star, Hero hero, GameEngine engine) {
        onTouch((PrizeItems) star, hero, engine);
        hero.setTookStar(true);
        engine.getSoundManager().playSuperStar();
        hero.setTimer();
    }

    private static void setHeroForm(Hero hero, int heroFormType) {
        HeroForm newForm = null;
        ImageLoader imageLoader = ImageLoader.getInstance();
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(heroFormType);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(heroFormType);

        if (heroFormType == HeroForm.SUPER) {
            newForm = new HeroForm(leftFrames, rightFrames, true, false, hero.getType());
        } else if (heroFormType == HeroForm.FIRE) {
            newForm = new HeroForm(leftFrames, rightFrames, true, true, hero.getType());
        }

        hero.setHeroForm(newForm);
    }

    public static void reveal(PrizeItems prize) {
        prize.setY(prize.getY() - 48);
        prize.setRevealed(true);
    }

    public static void update(PrizeItems prize) {
        if (prize.isRevealed()) {
            Physics.updateLocation(prize);
        }
    }

    public static void update(FireFlower flower) {
    }

    public static void update(SuperStar star) {
        Physics.updateLocation(star);
        setTimerToJump(star);
    }

    public static void setTimerToJump(SuperStar star) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (Math.floor(star.getY()) == (720 - 96 - 48 + 1) && !star.isJumping()) {
                    star.setJumping(true);
                    star.setVelY(7);
                }
                star.setJumpTimerActivated(false);
            }
        };

        if (!star.isJumpTimerActivated()
                && Math.floor(star.getY()) == (720 - 96 - 48 + 1)
                && !star.isJumping()
                && !star.isFalling()) {
            star.setJumpTimerActivated(true);
            Timer timer = new Timer();
            timer.schedule(task, 1000);
        }
    }

    public static void draw(PrizeItems prize, Graphics g) {
        if (prize.isRevealed()) {
            EntityRenderer.draw(prize, g);
        }
    }
}
