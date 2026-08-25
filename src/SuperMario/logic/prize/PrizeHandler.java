package SuperMario.logic.prize;

import SuperMario.config.GameConstants;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.hero.HeroFormLogic;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroForm;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.PrizeItems;
import SuperMario.model.prize.PrizeType;
import SuperMario.model.prize.SuperStar;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Central dispatcher for all prize gameplay behavior (touch, reveal, update, draw).
 */
public final class PrizeHandler {

    private PrizeHandler() {
    }

    public static void onTouch(Prize prize, Hero hero, GameEngine engine) {
        if (prize instanceof Coin) {
            onTouchCoin((Coin) prize, hero, engine);
        } else if (prize instanceof SuperStar) {
            onTouchSuperStar((SuperStar) prize, hero, engine);
        } else if (prize instanceof PrizeItems) {
            onTouchPrizeItem((PrizeItems) prize, hero, engine);
        }
    }

    public static void reveal(Prize prize) {
        if (prize instanceof Coin) {
            revealCoin((Coin) prize);
        } else if (prize instanceof PrizeItems) {
            revealPrizeItem((PrizeItems) prize);
        }
    }

    public static void update(Prize prize) {
        if (prize instanceof Coin) {
            updateCoin((Coin) prize);
        } else if (prize instanceof SuperStar) {
            updateSuperStar((SuperStar) prize);
        } else if (prize instanceof PrizeItems) {
            updatePrizeItem((PrizeItems) prize);
        }
    }

    public static void draw(Prize prize, Graphics g) {
        if (prize instanceof Coin) {
            drawCoin((Coin) prize, g);
        } else if (prize instanceof PrizeItems) {
            drawPrizeItem((PrizeItems) prize, g);
        }
    }

    private static void onTouchCoin(Coin coin, Hero hero, GameEngine engine) {
        if (!coin.isAcquired()) {
            coin.setAcquired(true);
            HeroLogic.acquirePoints(hero, coin.getPoint());
            HeroLogic.acquireCoin(hero);
            engine.getSoundManager().playCoin();
        }
    }

    private static void onTouchPrizeItem(PrizeItems prize, Hero hero, GameEngine engine) {
        switch (prize.getType()) {
            case SUPER_MUSHROOM:
            case FIRE_FLOWER:
                onTouchPowerUp(prize, hero, engine);
                engine.getSoundManager().playPowerUp();
                break;
            case HEART_MUSHROOM:
                HeroLogic.acquirePoints(hero, prize.getPoint());
                hero.setRemainingLives(hero.getRemainingLives() + 1);
                engine.getSoundManager().playOneUp();
                break;
            default:
                break;
        }
    }

    private static void onTouchSuperStar(SuperStar star, Hero hero, GameEngine engine) {
        onTouchPowerUp(star, hero, engine);
        hero.setTookStar(true);
        engine.getSoundManager().playSuperStar();
        HeroLogic.setTimer(hero);
    }

    private static void onTouchPowerUp(PrizeItems prize, Hero hero, GameEngine engine) {
        HeroLogic.acquirePoints(hero, prize.getPoint());

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

    private static void revealCoin(Coin coin) {
        coin.setRevealed(true);
    }

    private static void revealPrizeItem(PrizeItems prize) {
        prize.setY(prize.getY() - 48);
        prize.setRevealed(true);
    }

    private static void updateCoin(Coin coin) {
        if (coin.isRevealed()) {
            coin.setY(coin.getY() - 5);
        }
    }

    private static void updatePrizeItem(PrizeItems prize) {
        if (prize.isRevealed() && prize.getType() != PrizeType.FIRE_FLOWER) {
            Physics.updateLocation(prize);
        }
    }

    private static void updateSuperStar(SuperStar star) {
        Physics.updateLocation(star);
        scheduleJump(star);
    }

    private static void scheduleJump(SuperStar star) {
        if (star.getJumpDelayTicks() == 0
                && Math.floor(star.getY()) == (720 - 96 - 48 + 1)
                && !star.isJumping()
                && !star.isFalling()) {
            star.setJumpDelayTicks(GameConstants.msToTicks(GameConstants.SUPER_STAR_JUMP_DELAY_MS));
        }
    }

    private static void drawCoin(Coin coin, Graphics g) {
        if (coin.isRevealed()) {
            EntityRenderer.drawSprite(coin, g);
        }
    }

    private static void drawPrizeItem(PrizeItems prize, Graphics g) {
        if (prize.isRevealed()) {
            EntityRenderer.drawSprite(prize, g);
        }
    }

    private static void setHeroForm(Hero hero, int heroFormType) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(heroFormType);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(heroFormType);

        HeroForm newForm = null;
        if (heroFormType == HeroForm.SUPER) {
            newForm = HeroFormLogic.createForm(leftFrames, rightFrames, true, false, hero.getType());
        } else if (heroFormType == HeroForm.FIRE) {
            newForm = HeroFormLogic.createForm(leftFrames, rightFrames, true, true, hero.getType());
        }

        hero.setHeroForm(newForm);
    }
}
