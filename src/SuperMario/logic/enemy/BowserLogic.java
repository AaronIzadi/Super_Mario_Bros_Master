package SuperMario.logic.enemy;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.logic.map.BossHudLogic;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.enemy.bowser.Fire;
import SuperMario.model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class BowserLogic {

    private BowserLogic() {
    }

    public static void draw(Bowser bowser, Graphics g) {
        if (Math.ceil(bowser.getVelY()) < 0) {
            BufferedImage style = bowser.isToRight()
                    ? ImageLoader.getInstance().getBossUpSideRight()
                    : ImageLoader.getInstance().getBossUpSideLeft();
            bowser.setStyle(style);
        } else {
            animate(bowser);
        }
        EntityRenderer.drawSprite(bowser, g);
    }

    public static void setHp(Bowser bowser, int hp) {
        bowser.setHpValue(hp);
        BossHudLogic.setStyle(bowser.getHitPoints(), hp);
        stopMoving(bowser);
        bowser.setHpRecoveryTicks(GameConstants.msToTicks(GameConstants.BOWSER_HP_RECOVERY_MS));
    }

    public static void animate(Bowser bowser) {
        Animation animation = bowser.isToRight() ? bowser.getRightAnimation() : bowser.getLeftAnimation();
        if (animation == null) {
            return;
        }
        boolean isAnimationTicked = animation.animate(10);
        if (isAnimationTicked) {
            bowser.setStyle(animation.getCurrentFrame());
        }
    }

    public static void update(Bowser bowser) {
        Hero hero = bowser.getHero();
        if (hero.getY() + hero.getDimension().getHeight() - 1 == 720 - (2 * 48)) {
            if (Math.abs(hero.getX() - bowser.getX()) >= (8 * 48)) {
                moveFaster(bowser, bowser.isToRight());
            } else if (Math.abs(hero.getX() - bowser.getX()) <= (3 * 48)) {
                moveNormal(bowser, bowser.isToRight());
            }
        } else {
            moveNormal(bowser, bowser.isToRight());
            bowser.setVelX(bowser.getVelX() * -1);
        }

        Physics.updateLocation(bowser);
    }

    public static void attack(Bowser bowser) {
        if (!bowser.isCoolDownFinished()) {
            return;
        }

        Hero hero = bowser.getHero();
        int random;

        if (bowser.getHpValue() > 10) {
            random = (int) (Math.random() * 3);
        } else {
            random = (int) (Math.random() * 4);
        }

        if (random == 0 && Math.abs(hero.getX() - bowser.getX()) >= (6 * 48)
                && Math.abs(hero.getX() - bowser.getX()) <= (10 * 48)) {
            fire(bowser);
        } else if (random == 1 && Math.abs(hero.getX() - bowser.getX()) <= (2 * 48)) {
            grabAttack(bowser, hero);
        } else if (random == 2 && HeroLogic.getOnLandStandingTimer(hero)) {
            jumpAttack(bowser);
        } else if (random == 3) {
            bomb(bowser);
        }
    }

    private static void fire(Bowser bowser) {
        startAttackCooldown(bowser, GameConstants.BOWSER_ATTACK_COOLDOWN_MS);

        BufferedImage style = bowser.isToRight()
                ? ImageLoader.getInstance().getFireballRight()
                : ImageLoader.getInstance().getFireballLeft();
        double x = bowser.isToRight() ? bowser.getX() + 9 : bowser.getX() - 1;

        int random = (int) (Math.random() * 2);
        if (random == 0) {
            bowser.getFire().add(new Fire(x, bowser.getY() + 24, style, bowser.isToRight()));
        } else {
            bowser.getFire().add(new Fire(x, bowser.getY() + 72, style, bowser.isToRight()));
        }
        GameEngine.getInstance().getSoundManager().playBowserFireBall();
    }

    private static void bomb(Bowser bowser) {
        startAttackCooldown(bowser, 4000);

        double x = bowser.isToRight() ? bowser.getX() + 78 : bowser.getX();
        double y = bowser.getY() + 68;

        bowser.getBomb().add(new Bomb(x, y, ImageLoader.getInstance().getBomb()));
        GameEngine.getInstance().getSoundManager().playBowserFireBall();
    }

    private static void jumpAttack(Bowser bowser) {
        startAttackCooldown(bowser, 4000);

        if (!bowser.isJumping()) {
            bowser.setJumping(true);
            bowser.setVelY(7);
            bowser.setHasTouchedGround(false);
        }
    }

    private static void grabAttack(Bowser bowser, Hero hero) {
        if (!bowser.isGrabAttackOn()) {
            bowser.setCoolDownFinished(false);
            bowser.setGrabAttackOn(true);
            bowser.setCanHurt(true);
            bowser.setGrabReactionTicks(GameConstants.msToTicks(GameConstants.BOWSER_GRAB_ATTACK_WAIT_MS));
        }
    }

    private static void startAttackCooldown(Bowser bowser, int durationMs) {
        bowser.setCoolDownFinished(false);
        bowser.setCooldownTicks(GameConstants.msToTicks(durationMs));
    }

    public static void canJump(Bowser bowser, boolean isFar) {
        bowser.setJumpIntent(false);

        if (!isFar) {
            int random = (int) (Math.random() * 8);
            if (random == 1) {
                bowser.setJumpIntent(true);
            }
        } else {
            bowser.setJumpIntent(true);
        }
    }

    public static void jump(Bowser bowser) {
        if (!bowser.isJumping() && bowser.isJumpIntent()) {
            bowser.setJumping(true);
            bowser.setVelY(7);
        }

        bowser.setJumpIntent(false);
    }

    public static void moveFaster(Bowser bowser, boolean toRight) {
        if (toRight) {
            bowser.setVelX(4.5);
        } else {
            bowser.setVelX(-4.5);
        }
    }

    public static void moveNormal(Bowser bowser, boolean toRight) {
        if (toRight) {
            bowser.setVelX(1.5);
        } else {
            bowser.setVelX(-1.5);
        }
    }

    public static void stopMoving(Bowser bowser) {
        bowser.setVelX(0);
    }

    public static void setFrames(Bowser bowser) {
        bowser.setRightAnimation(new Animation(bowser.getRightFrames()));
        bowser.setLeftAnimation(new Animation(bowser.getLeftFrames()));
    }

    public static void draw(Bomb bomb, Graphics g) {
        EntityRenderer.drawSprite(bomb, g);
        if (bomb.hasIntersect()) {
            bomb.setStyle(ImageLoader.getInstance().getBombOn());
        }
    }

    public static void setHasIntersect(Bomb bomb, boolean hasIntersect) {
        bomb.setHasIntersectFlag(hasIntersect);
        if (hasIntersect && bomb.getExplodeTicks() == 0 && !bomb.isExploded()) {
            bomb.setExplodeTicks(GameConstants.msToTicks(GameConstants.BOMB_EXPLODE_DELAY_MS));
        }
    }

    public static void update(Fire fire) {
        Physics.updateLocation(fire);
    }

    public static void update(Bomb bomb) {
        Physics.updateLocation(bomb);
    }

    public static void handleGrabTimeout(Hero hero, Bowser bowser, GameEngine engine) {
        if (!hero.isGrabbed()) {
            return;
        }
        hero.setGrabbed(false);
        HeroLogic.onTouchEnemy(hero, engine, 0);
        double x = bowser.isToRight() ? (104 + 96 - 24) : (-96 + 24);
        hero.setX(hero.getX() + x);
        hero.setNumberOfTryToEscape(0);
        moveNormal(bowser, bowser.isToRight());
        bowser.setGrabAttackOn(false);
        bowser.setPostGrabRecoveryTicks(GameConstants.msToTicks(GameConstants.GRAB_RECOVERY_MS));
    }
}
