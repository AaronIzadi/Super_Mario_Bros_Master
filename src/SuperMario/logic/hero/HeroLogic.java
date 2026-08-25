package SuperMario.logic.hero;

import SuperMario.config.GameConstants;
import SuperMario.graphic.manager.Camera;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.logic.weapon.WeaponLogic;
import SuperMario.model.hero.Hero;
import SuperMario.model.hero.HeroForm;
import SuperMario.model.hero.HeroType;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class HeroLogic {

    private HeroLogic() {
    }

    public static void initializeNewHero(Hero hero) {
        initializeHero(hero, HeroForm.SMALL, false, false);
    }

    public static void initializeHero(Hero hero, int heroForm, boolean isSuper, boolean canShootFire) {
        hero.setRemainingLives(3);
        hero.setPoints(0);
        hero.setCoins(0);
        hero.setInvincibilityTimer(0);
        hero.setToRight(true);
        hero.setTookStar(false);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.setHeroType(hero.getType());
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(heroForm);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(heroForm);

        HeroForm form = HeroFormLogic.createForm(leftFrames, rightFrames, isSuper, canShootFire, hero.getType());
        hero.setHeroForm(form);
        hero.setStyle(HeroFormLogic.getCurrentStyle(form, hero.getToRight(), false, false, false));
    }

    public static void draw(Hero hero, Graphics g) {
        boolean movingInX = (hero.getVelX() != 0);
        boolean movingInY = (hero.getVelY() != 0);

        hero.setStyle(HeroFormLogic.getCurrentStyle(hero.getHeroForm(), hero.getToRight(), movingInX, movingInY, hero.isCrouching()));

        EntityRenderer.drawSprite(hero, g);

        Axe axe = hero.getAxe();
        if (axe != null) {
            if (!axe.isReleased()) {
                if (hero.getToRight()) {
                    axe.setX(hero.getX() + 24);
                } else {
                    axe.setX(hero.getX() - GameConstants.TILE_SIZE);
                }
                axe.setVelX(hero.getVelX());
                axe.setVelY(hero.getVelY());
                axe.setY(hero.getY() - GameConstants.AXE_HOLD_OFFSET_Y);
            }
            WeaponLogic.draw(axe, g);
        }
    }

    public static void jump(Hero hero) {
        setVelYToJump(hero, jumpVelocity(hero.getType()));
    }

    public static void jumpOnEnemy(Hero hero) {
        setVelYToJump(hero, jumpOnEnemyVelocity(hero.getType()));
    }

    public static void jumpOnSlime(Hero hero) {
        setVelYToJump(hero, jumpOnSlimeVelocity(hero.getType()));
    }

    public static void move(Hero hero, boolean toRight, Camera camera) {
        if (!hero.isCrouching()) {
            int speed = moveSpeed(hero.getType());
            if (toRight) {
                hero.setVelX(speed);
            } else if (camera.getX() < hero.getX()) {
                hero.setVelX(-speed);
            }
            hero.setToRight(toRight);
        }
    }

    public static void sit(Hero hero) {
        if (hero.isSuper() && !hero.isJumping() && hero.getVelX() == 0) {
            hero.setCrouching(true);
            hero.getDimension().height = GameConstants.CROUCH_HEIGHT;
            hero.setY(hero.getY() + GameConstants.CROUCH_OFFSET);
        }
    }

    public static void getUp(Hero hero) {
        if (hero.isCrouching()) {
            hero.setCrouching(false);
            hero.setY(hero.getY() - GameConstants.CROUCH_OFFSET);
        }
    }

    public static void setVelYToJump(Hero hero, int velY) {
        if (!hero.isJumping() && !hero.isFalling() && !hero.isCrouching()) {
            hero.setJumping(true);
            hero.setVelY(velY);
        }
    }

    public static boolean getOnLandStandingTimer(Hero hero) {
        if (hero.getY() + hero.getDimension().getHeight() - 1 == GameConstants.GROUND_Y) {
            if (hero.getStandingStart() == 0) {
                hero.setStandingStart(System.currentTimeMillis());
            } else {
                hero.setStandingTimer(System.currentTimeMillis() - hero.getStandingStart());
            }
        } else {
            hero.setStandingStart(0);
            hero.setStandingTimer(0);
        }
        return hero.getStandingTimer() >= GameConstants.STANDING_TIMER_MS;
    }

    public static void setTimer(Hero hero) {
        hero.setStarPowerTicks(GameConstants.msToTicks(GameConstants.STAR_POWER_DURATION_MS));
    }

    public static void setTimerToRun(Hero hero) {
        hero.setStarRunTicks(GameConstants.msToTicks(GameConstants.STAR_RUN_DURATION_MS));
    }

    public static boolean onTouchEnemy(Hero hero, GameEngine engine, int losingCoins) {
        if (!hero.hasStarPower()) {
            if (!hero.getHeroForm().isSuper()) {
                heroDies(hero, engine, 20, losingCoins);
                return true;
            } else {
                engine.getCameraManager().shakeCamera();
                hero.getHeroForm().setSuper(false);
                hero.getHeroForm().setCanShootFire(false);
                HeroFormLogic.resetToSmallOnDamage(hero.getHeroForm(), engine.getImageLoader());
                hero.setDimension(GameConstants.HERO_DEFAULT_SIZE, GameConstants.HERO_DEFAULT_SIZE);
                hero.setY(hero.getY() + hero.getDimension().getHeight());
                return false;
            }
        }
        return false;
    }

    public static void onTouchBorder(Hero hero, GameEngine engine, int losingCoins) {
        heroDies(hero, engine, 30, losingCoins);
        engine.getSoundManager().playHeroFalls();
    }

    public static void heroDies(Hero hero, GameEngine engine, int lostScore, int losingCoins) {
        hero.setRemainingLives(hero.getRemainingLives() - 1);
        hero.setPoints(hero.getPoints() > lostScore ? hero.getPoints() - lostScore : 0);
        hero.setCoins(hero.getCoins() > losingCoins ? hero.getCoins() - losingCoins : 0);
        if (hero.getRemainingLives() == 0) {
            engine.getSoundManager().playGameOver();
        } else {
            engine.getSoundManager().playHeroDies();
        }
        hero.getHeroForm().setSuper(false);
        hero.getHeroForm().setCanShootFire(false);
        HeroFormLogic.resetToSmallOnDamage(hero.getHeroForm(), engine.getImageLoader());
        hero.setDimension(GameConstants.HERO_DEFAULT_SIZE, GameConstants.HERO_DEFAULT_SIZE);
    }

    public static Fireball fire(Hero hero) {
        return HeroFormLogic.createFireball(hero.getHeroForm(), hero.getToRight(), hero.getX(), hero.getY());
    }

    public static boolean canActivateAxe(Hero hero) {
        return hero.getCoins() >= 3 && hero.isSuper() && hero.getAxeCooldownTicks() <= 0;
    }

    public static void activateAxe(Hero hero) {
        if (canActivateAxe(hero)) {
            hero.setCoins(hero.getCoins() - 3);
            hero.setAxe(WeaponLogic.createAxe(hero));
        }
    }

    public static void deactivateAxe(Hero hero) {
        hero.setAxe(null);
        hero.setAxeActivated(false);
        hero.setAxeCooldownTicks(GameConstants.msToTicks(GameConstants.AXE_COOLDOWN_MS));
    }

    public static void throwAxe(Hero hero) {
        WeaponLogic.setReleased(hero.getAxe(), true, hero.getX());
    }

    public static void acquireCoin(Hero hero) {
        hero.setCoins(hero.getCoins() + 1);
        if (hero.getType() == HeroType.TOAD) {
            hero.setCoins(hero.getCoins() + 1);
        }
    }

    public static void acquirePoints(Hero hero, int point) {
        hero.setPoints(hero.getPoints() + point);
    }

    private static int jumpVelocity(int type) {
        switch (type) {
            case HeroType.LUIGI:
                return 14;
            case HeroType.PRINCESS_PEACH:
                return 12;
            case HeroType.MARIO:
            case HeroType.TOAD:
            case HeroType.ROSALINA:
            default:
                return 10;
        }
    }

    private static int jumpOnEnemyVelocity(int type) {
        switch (type) {
            case HeroType.LUIGI:
                return 7;
            case HeroType.PRINCESS_PEACH:
                return 6;
            case HeroType.TOAD:
                return 15;
            case HeroType.MARIO:
            case HeroType.ROSALINA:
            default:
                return 5;
        }
    }

    private static int jumpOnSlimeVelocity(int type) {
        switch (type) {
            case HeroType.LUIGI:
                return 21;
            case HeroType.PRINCESS_PEACH:
                return 18;
            case HeroType.MARIO:
            case HeroType.TOAD:
            case HeroType.ROSALINA:
            default:
                return 15;
        }
    }

    private static int moveSpeed(int type) {
        switch (type) {
            case HeroType.PRINCESS_PEACH:
                return 6;
            case HeroType.ROSALINA:
                return 7;
            case HeroType.MARIO:
            case HeroType.LUIGI:
            case HeroType.TOAD:
            default:
                return 5;
        }
    }

    public static void applyCharacterTraits(Hero hero) {
        if (hero.getType() == HeroType.TOAD) {
            hero.setRemainingLives(5);
        }
    }
}
