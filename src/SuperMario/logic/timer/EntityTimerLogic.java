package SuperMario.logic.timer;

import SuperMario.config.GameConstants;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.model.enemy.KoopaTroopa;
import SuperMario.model.enemy.Piranha;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.Obstacle;
import SuperMario.model.obstacle.Slime;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.SuperStar;

public final class EntityTimerLogic {

    private EntityTimerLogic() {
    }

    public static void tick(Map map, GameEngine engine) {
        if (map == null) {
            return;
        }

        tickHero(map.getHero(), map, engine);
        for (Enemy enemy : map.getEnemies()) {
            tickEnemy(enemy);
        }
        tickBowser(map.getBowser());
        for (Prize prize : map.getRevealedPrizes()) {
            if (prize instanceof SuperStar) {
                tickSuperStar((SuperStar) prize);
            }
        }
        for (Obstacle obstacle : map.getObstacles()) {
            if (obstacle instanceof Slime) {
                tickSlime((Slime) obstacle);
            }
        }
        GameTimer.tick();
    }

    public static void clear() {
        GameTimer.clear();
    }

    private static void tickHero(Hero hero, Map map, GameEngine engine) {
        if (hero.getStarPowerTicks() > 0) {
            hero.setStarPowerTicks(hero.getStarPowerTicks() - 1);
            if (hero.getStarPowerTicks() == 0) {
                hero.setTookStar(false);
                if (!engine.isMute()) {
                    engine.getSoundManager().resumeBackground();
                }
            }
        }

        if (hero.getStarRunTicks() > 0) {
            hero.setStarRunTicks(hero.getStarRunTicks() - 1);
            if (hero.getStarRunTicks() == 0) {
                hero.setTookStar(false);
            }
        }

        if (hero.getAxeCooldownTicks() > 0) {
            hero.setAxeCooldownTicks(hero.getAxeCooldownTicks() - 1);
        }

        if (hero.isGrabbed() && hero.getGrabTimeoutTicks() > 0) {
            hero.setGrabTimeoutTicks(hero.getGrabTimeoutTicks() - 1);
            if (hero.getGrabTimeoutTicks() == 0) {
                BowserLogic.handleGrabTimeout(hero, map.getBowser(), engine);
            }
        }
    }

    private static void tickEnemy(Enemy enemy) {
        if (enemy instanceof KoopaTroopa) {
            tickKoopa((KoopaTroopa) enemy);
        } else if (enemy instanceof Piranha) {
            tickPiranha((Piranha) enemy);
        }
    }

    private static void tickKoopa(KoopaTroopa koopa) {
        if (!koopa.isHit() || koopa.getShellRecoveryTicks() <= 0) {
            return;
        }
        koopa.setShellRecoveryTicks(koopa.getShellRecoveryTicks() - 1);
        if (koopa.getShellRecoveryTicks() == 0) {
            koopa.setHit(false);
        }
    }

    private static void tickPiranha(Piranha piranha) {
        if (piranha.getMovementDelayTicks() <= 0) {
            return;
        }
        piranha.setMovementDelayTicks(piranha.getMovementDelayTicks() - 1);
        if (piranha.getMovementDelayTicks() == 0) {
            piranha.setVelY(piranha.getPendingVelY());
        }
    }

    private static void tickBowser(Bowser bowser) {
        if (bowser == null) {
            return;
        }

        if (bowser.getCooldownTicks() > 0) {
            bowser.setCooldownTicks(bowser.getCooldownTicks() - 1);
            if (bowser.getCooldownTicks() == 0) {
                bowser.setCoolDownFinished(true);
            }
        }

        if (bowser.getHpRecoveryTicks() > 0) {
            bowser.setHpRecoveryTicks(bowser.getHpRecoveryTicks() - 1);
            if (bowser.getHpRecoveryTicks() == 0) {
                BowserLogic.moveNormal(bowser, bowser.isToRight());
            }
        }

        if (bowser.getGrabReactionTicks() > 0) {
            bowser.setGrabReactionTicks(bowser.getGrabReactionTicks() - 1);
            if (bowser.getGrabReactionTicks() == 0 && bowser.getHero() != null && !bowser.getHero().isGrabbed()) {
                BowserLogic.moveNormal(bowser, bowser.isToRight());
                bowser.setCoolDownFinished(true);
            }
        }

        if (bowser.getPostGrabRecoveryTicks() > 0) {
            bowser.setPostGrabRecoveryTicks(bowser.getPostGrabRecoveryTicks() - 1);
            if (bowser.getPostGrabRecoveryTicks() == 0) {
                bowser.setCanHurt(true);
                bowser.setCoolDownFinished(true);
                bowser.setGrabAttackOn(false);
            }
        }

        for (Bomb bomb : bowser.getBomb()) {
            tickBomb(bomb);
        }
    }

    private static void tickBomb(Bomb bomb) {
        if (bomb.getExplodeTicks() > 0) {
            bomb.setExplodeTicks(bomb.getExplodeTicks() - 1);
            if (bomb.getExplodeTicks() == 0) {
                bomb.setStyle(ImageLoader.getInstance().getBombExplode());
                bomb.setExploded(true);
            }
        }

        if (bomb.isExploded() && !bomb.isVanishScheduled()) {
            bomb.setVanishScheduled(true);
            bomb.setVanishTicks(GameConstants.msToTicks(GameConstants.BOMB_VANISH_DELAY_MS));
        }

        if (bomb.getVanishTicks() > 0) {
            bomb.setVanishTicks(bomb.getVanishTicks() - 1);
            if (bomb.getVanishTicks() == 0) {
                bomb.setStyle(null);
                bomb.setTimeToVanish(true);
            }
        }
    }

    private static void tickSuperStar(SuperStar star) {
        if (star.getJumpDelayTicks() > 0) {
            star.setJumpDelayTicks(star.getJumpDelayTicks() - 1);
            if (star.getJumpDelayTicks() == 0
                    && Math.floor(star.getY()) == (720 - 96 - 48 + 1)
                    && !star.isJumping()) {
                star.setJumping(true);
                star.setVelY(7);
            }
        }
    }

    private static void tickSlime(Slime slime) {
        if (slime.getRestyleTicks() > 0) {
            slime.setRestyleTicks(slime.getRestyleTicks() - 1);
            if (slime.getRestyleTicks() == 0) {
                slime.setOnTouchFlag(false);
            }
        }
    }
}
