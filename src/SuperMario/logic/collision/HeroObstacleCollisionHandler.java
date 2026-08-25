package SuperMario.logic.collision;

import SuperMario.config.GameConstants;
import SuperMario.graphic.view.states.GameState;
import SuperMario.graphic.view.states.MapSelection;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.model.GameObject;
import SuperMario.model.enemy.*;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.Prize;

import java.awt.*;
import java.util.ArrayList;

final class HeroObstacleCollisionHandler {

    private HeroObstacleCollisionHandler() {
    }

    static void checkBottomCollisions(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        Hero hero = callbacks.getHero();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<Enemy> enemies = currentMap.getEnemies();
        ArrayList<GameObject> toBeRemoved = ctx.toBeRemoved();

        Rectangle heroBottomBounds = hero.getBottomBounds();
        boolean heroHasBottomIntersection = false;

        for (Obstacle obstacle : obstacles) {
            if (obstacle instanceof Brick && (int) hero.getBottomBounds().getY() >= GameConstants.GROUND_BRICK_Y) {
                ((Brick) obstacle).setTimer(0);
            }

            Rectangle obstacleTopBounds = obstacle.getTopBounds();
            if (heroBottomBounds.intersects(obstacleTopBounds)) {
                if (callbacks.getEngine().getUserData().getWorldNumber() == MapSelection.BOSS_FIGHT.getWorldNumber()) {
                    if (obstacle instanceof Brick && BrickLogic.isTimeToBreak((Brick) obstacle)) {
                        toBeRemoved.add(obstacle);
                    }
                }

                if (!(obstacle instanceof Hole)) {
                    hero.setY(obstacle.getY() - hero.getDimension().height + 1);
                    hero.setFalling(false);
                    hero.setVelY(0);
                    heroHasBottomIntersection = true;
                    if (obstacle instanceof Slime) {
                        BrickLogic.setOnTouch((Slime) obstacle, true);
                        HeroLogic.jumpOnSlime(hero);
                        callbacks.getEngine().getSoundManager().playJump();
                    }
                    if (obstacle instanceof CrossoverTunnel
                            && !((CrossoverTunnel) obstacle).isRevealed()
                            && callbacks.getEngine().getInputManager().getInputReceiver().isDown()) {
                        if (callbacks.getEngine().getStateManager().getGameState() == GameState.RUNNING) {
                            callbacks.getEngine().getSoundManager().playPipe();
                            callbacks.setXBeforeCrossover(hero.getX());
                            callbacks.setYBeforeCrossover(hero.getY());
                            ((CrossoverTunnel) obstacle).setRevealed(true);
                            callbacks.getEngine().getStateManager().setGameState(GameState.CROSSOVER);
                            if (callbacks.getEngine().getUserData().getWorldNumber() == 0) {
                                callbacks.createCrossover(
                                        MapSelection.CROSSOVER_1.getMapPath(MapSelection.CROSSOVER_1.getWorldNumber()), hero);
                            } else if (callbacks.getEngine().getUserData().getWorldNumber() == 1) {
                                callbacks.createCrossover(
                                        MapSelection.CROSSOVER_2.getMapPath(MapSelection.CROSSOVER_2.getWorldNumber()), hero);
                            } else {
                                callbacks.createCrossover(
                                        MapSelection.CROSSOVER_3.getMapPath(MapSelection.CROSSOVER_3.getWorldNumber()), hero);
                            }
                        } else {
                            callbacks.getEngine().getStateManager().setGameState(GameState.RUNNING);
                            hero.setCrouching(false);
                            hero.setLocation(callbacks.getXBeforeCrossover(), callbacks.getYBeforeCrossover());
                        }
                    }
                } else {
                    hero.setFalling(true);
                }
            }
        }

        hero.setFalling(!heroHasBottomIntersection);

        for (Enemy enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (heroBottomBounds.intersects(enemyTopBounds) && !(enemy instanceof Spiny) && !(enemy instanceof Piranha)) {
                if (enemy instanceof Bowser) {
                    int newHP = ((Bowser) enemy).getHp() > 3 ? (((Bowser) enemy).getHp() - 3) : 0;
                    BowserLogic.setHp((Bowser) enemy, newHP);
                    callbacks.getEngine().getSoundManager().playStomp();
                    hero.setFalling(false);
                    HeroLogic.jump(hero);
                    callbacks.getEngine().getSoundManager().playJump();
                    if (callbacks.checkIfBowserDies()) {
                        toBeRemoved.add(enemy);
                        callbacks.getMap().setBowser(null);
                        callbacks.getEngine().getSoundManager().heroWinsOverBowser();
                    }
                } else if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = (KoopaTroopa) enemy;
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        EnemyLogic.moveAfterHit(koopaTroopa);
                        HeroLogic.setTimerToRun(hero);
                    } else {
                        callbacks.acquirePoints(2);
                        toBeRemoved.add(enemy);
                        callbacks.getEngine().getSoundManager().playStomp();
                    }
                    hero.setFalling(false);
                    HeroLogic.jumpOnEnemy(hero);
                    callbacks.getEngine().getSoundManager().playJump();
                } else {
                    callbacks.acquirePoints(1);
                    toBeRemoved.add(enemy);
                    callbacks.getEngine().getSoundManager().playStomp();
                    hero.setFalling(false);
                    HeroLogic.jumpOnEnemy(hero);
                    callbacks.getEngine().getSoundManager().playJump();
                }
            }
        }

        if (hero.getY() >= callbacks.getMap().getBottomBorder()) {
            HeroLogic.onTouchBorder(hero, callbacks.getEngine(), callbacks.calculateLosingCoins());
            if (callbacks.isChecked()) {
                callbacks.reLoadCheckPoint(callbacks.getXHero(), callbacks.getYHero());
            } else {
                callbacks.resetCurrentMap(callbacks.getEngine());
            }
        }

        CollisionObjectRemoval.removeObjects(ctx, toBeRemoved);
    }

    static void checkTopCollisions(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        Hero hero = callbacks.getHero();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        Rectangle heroTopBounds = hero.getTopBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBottomBounds = obstacle.getBottomBounds();
            if (!(obstacle instanceof Hole) && !(obstacle instanceof CheckPoint)
                    && heroTopBounds.intersects(obstacleBottomBounds)) {
                hero.setVelY(0);
                hero.setY(obstacle.getY() + obstacle.getDimension().height);
                if (obstacle instanceof Brick) {
                    Prize prize = BrickLogic.reveal((Brick) obstacle, callbacks.getEngine());
                    if (prize != null) {
                        currentMap.addRevealedPrize(prize);
                    }
                }
            } else if (obstacle instanceof CheckPoint && heroTopBounds.intersects(obstacleBottomBounds)) {
                if (!((CheckPoint) obstacle).isRevealed()) {
                    callbacks.pauseInCheckPoint();
                } else {
                    hero.setVelY(0);
                    hero.setY(obstacle.getY() + obstacle.getDimension().height);
                }
            }
        }
    }

    static void checkHeroHorizontalCollision(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        Hero hero = callbacks.getHero();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<Enemy> enemies = currentMap.getEnemies();
        ArrayList<GameObject> toBeRemoved = ctx.toBeRemoved();

        boolean heroDies = false;
        boolean toRight = hero.getToRight();
        Rectangle heroBounds = toRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = !toRight ? obstacle.getRightBounds() : obstacle.getLeftBounds();
            if (heroBounds.intersects(obstacleBounds)) {
                hero.setVelX(0);
                if (toRight) {
                    hero.setX(obstacle.getX() - hero.getDimension().width);
                } else {
                    hero.setX(obstacle.getX() + obstacle.getDimension().width);
                }
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (heroBounds.intersects(enemyBounds) && !hero.isFalling()) {
                if (!hero.hasStarPower()) {
                    if (enemy instanceof Bowser && ((Bowser) enemy).isGrabAttackOn()) {
                        if (!hero.isGrabbed()) {
                            hero.setGrabbed(true);
                            BowserLogic.stopMoving(callbacks.getMap().getBowser());
                            ((Bowser) enemy).setCanHurt(false);
                            BowserCollisionHandler.setTimerForGrabAttack(ctx);
                        }
                    } else if (enemy instanceof Bowser && ((Bowser) enemy).canHurt()) {
                        heroDies = HeroLogic.onTouchEnemy(hero, callbacks.getEngine(), callbacks.calculateLosingCoins());
                    } else {
                        heroDies = HeroLogic.onTouchEnemy(hero, callbacks.getEngine(), callbacks.calculateLosingCoins());
                        if (enemy instanceof KoopaTroopa) {
                            ((KoopaTroopa) enemy).setHit(true);
                        } else if (enemy instanceof Goomba) {
                            toBeRemoved.add(enemy);
                        }
                    }
                } else {
                    if (enemy instanceof Bowser) {
                        BowserLogic.setHp((Bowser) enemy, ((Bowser) enemy).getHp() - 1);
                        if (callbacks.checkIfBowserDies()) {
                            toBeRemoved.add(enemy);
                            callbacks.getMap().setBowser(null);
                            callbacks.getEngine().getSoundManager().heroWinsOverBowser();
                        }
                    } else {
                        toBeRemoved.add(enemy);
                    }
                }
            }
        }

        CollisionObjectRemoval.removeObjects(ctx, toBeRemoved);

        if (heroDies) {
            if (callbacks.isChecked()) {
                callbacks.reLoadCheckPoint(callbacks.getXHero(), callbacks.getYHero());
            } else {
                callbacks.resetCurrentMap(callbacks.getEngine());
            }
        }
    }
}
