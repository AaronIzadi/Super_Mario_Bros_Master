package SuperMario.logic.collision;

import SuperMario.config.GameConstants;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.model.GameObject;
import SuperMario.model.enemy.*;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.enemy.bowser.Fire;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.Brick;
import SuperMario.model.obstacle.Hole;
import SuperMario.model.obstacle.Obstacle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

final class BowserCollisionHandler {

    private BowserCollisionHandler() {
    }

    static void checkBowserPossibleCollisions(CollisionContext ctx, GameObject object) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        Hero hero = callbacks.getHero();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        Rectangle bottomBounds = object.getBottomBounds();
        boolean toRight = object.isToRight();
        Rectangle horizontalBounds = toRight ? object.getRightBounds() : object.getLeftBounds();

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = !toRight ? obstacle.getRightBounds() : obstacle.getLeftBounds();
            if (horizontalBounds.intersects(obstacleBounds)) {
                object.setVelX(object.getVelX() * -1);
                if (toRight) {
                    object.setX(obstacle.getX() - object.getDimension().width);
                } else {
                    object.setX(obstacle.getX() + obstacle.getDimension().width);
                }
            }
        }

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleTopBounds = obstacle.getTopBounds();
            if (bottomBounds.intersects(obstacleTopBounds)) {
                if (!(obstacle instanceof Hole)) {
                    object.setY(obstacle.getY() - object.getDimension().height + 1);
                    object.setFalling(false);
                    object.setVelY(0);
                } else {
                    object.setFalling(true);
                }
            }
        }

        Rectangle topBounds = object.getTopBounds();
        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBottomBounds = obstacle.getBottomBounds();
            if (topBounds.intersects(obstacleBottomBounds)) {
                object.setVelY(0);
                object.setY(obstacle.getY() + obstacle.getDimension().height);
                if (obstacle instanceof Brick) {
                    BrickLogic.reveal((Brick) obstacle, callbacks.getEngine());
                }
            }
        }

        if (object.getY() + object.getDimension().height >= callbacks.getMap().getBottomBorder() - (2 * GameConstants.TILE_SIZE)) {
            if (object instanceof Bowser && !((Bowser) object).hasTouchedGround()) {
                callbacks.getEngine().getCameraManager().shakeCamera();
                if (hero.getBottomBounds().getY() >= GameConstants.GROUND_BRICK_Y) {
                    HeroLogic.onTouchEnemy(hero, callbacks.getEngine(), 0);
                }
                ((Bowser) object).setHasTouchedGround(true);
            } else if (object instanceof Bomb) {
                BowserLogic.setHasIntersect((Bomb) object, true);
            }
            object.setFalling(false);
        }
    }

    static void checkEnemyWeaponContact(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        for (Fire fire : callbacks.getMap().getBowser().getFire()) {
            checkEnemyWeaponCollision(ctx, fire);
        }
        for (Bomb bomb : callbacks.getMap().getBowser().getBomb()) {
            checkEnemyWeaponCollision(ctx, bomb);
        }
        CollisionObjectRemoval.removeObjects(ctx, ctx.toBeRemoved());
    }

    private static void checkEnemyWeaponCollision(CollisionContext ctx, GameObject object) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        Hero hero = callbacks.getHero();
        ArrayList<Enemy> enemies = currentMap.getEnemies();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<GameObject> toBeRemoved = ctx.toBeRemoved();

        Rectangle objectBounds = object.getBounds();
        if (object instanceof Bomb && ((Bomb) object).isExploded()) {
            objectBounds.y -= 48;
            objectBounds.x -= 48;
            objectBounds.height += 48;
            objectBounds.width += 48;
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (objectBounds.intersects(enemyBounds)) {
                if (enemy instanceof Bowser) {
                    if (object instanceof Bomb && ((Bomb) object).isExploded()) {
                        BowserLogic.setHp((Bowser) enemy, ((Bowser) enemy).getHp() - 1);
                        if (callbacks.checkIfBowserDies()) {
                            toBeRemoved.add(enemy);
                            callbacks.getMap().setBowser(null);
                            callbacks.getEngine().getSoundManager().heroWinsOverBowser();
                        }
                    }
                } else if (enemy instanceof Goomba) {
                    toBeRemoved.add(enemy);
                } else if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = (KoopaTroopa) enemy;
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        EnemyLogic.moveAfterHit(koopaTroopa);
                    } else {
                        toBeRemoved.add(enemy);
                    }
                } else if (enemy instanceof Spiny || enemy instanceof Piranha) {
                    toBeRemoved.add(enemy);
                }

                if (object instanceof Bomb) {
                    if (!((Bomb) object).hasIntersect() && !(enemy instanceof Bowser)) {
                        BowserLogic.setHasIntersect((Bomb) object, true);
                    } else if (((Bomb) object).hasIntersect() && ((Bomb) object).isExploded()) {
                        toBeRemoved.add(enemy);
                    }
                } else if (object instanceof Fire && !(enemy instanceof Bowser)) {
                    toBeRemoved.add(object);
                }
            }
        }

        if (objectBounds.intersects(hero.getBounds())) {
            if (object instanceof Bomb) {
                if (!((Bomb) object).hasIntersect()) {
                    BowserLogic.setHasIntersect((Bomb) object, true);
                } else if (((Bomb) object).hasIntersect() && ((Bomb) object).isExploded()) {
                    HeroLogic.onTouchEnemy(hero, callbacks.getEngine(), 0);
                    ((Bomb) object).setTimeToVanish(true);
                }
            } else if (object instanceof Fire) {
                toBeRemoved.add(object);
                HeroLogic.onTouchEnemy(hero, callbacks.getEngine(), 0);
            }
        }

        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = obstacle.getBounds();
            if (object instanceof Fire) {
                obstacleBounds = object.isToRight() ? obstacle.getLeftBounds() : obstacle.getRightBounds();
            }
            if (objectBounds.intersects(obstacleBounds)) {
                if (object instanceof Bomb) {
                    if (!((Bomb) object).hasIntersect()) {
                        BowserLogic.setHasIntersect((Bomb) object, true);
                    } else if (((Bomb) object).isExploded()) {
                        toBeRemoved.add(obstacle);
                    }
                } else if (object instanceof Fire) {
                    toBeRemoved.add(object);
                }
            }
        }

        if (object instanceof Bomb && ((Bomb) object).isTimeToVanish()) {
            toBeRemoved.add(object);
        }
    }

    static void updateGrabState(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Hero hero = callbacks.getHero();
        Bowser bowser = callbacks.getMap().getBowser();

        if (hero.getNumberOfTryToEscape() >= 10) {
            hero.setGrabbed(false);
            double x = bowser.isToRight() ? (104 + 96 - 24) : (-96 + 24);
            hero.setX(hero.getX() + x);
            hero.setNumberOfTryToEscape(0);
            Timer grabTimer = callbacks.getGrabTimer();
            if (grabTimer != null) {
                grabTimer.cancel();
            }
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    bowser.setCanHurt(true);
                    bowser.setCoolDownFinished(true);
                    BowserLogic.moveNormal(bowser, bowser.isToRight());
                    bowser.setGrabAttackOn(false);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 4000);
        } else {
            bowser.setCoolDownFinished(false);
        }
    }

    static void setTimerForGrabAttack(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Hero hero = callbacks.getHero();
        Bowser bowser = callbacks.getMap().getBowser();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (hero.isGrabbed()) {
                    hero.setGrabbed(false);
                    HeroLogic.onTouchEnemy(hero, callbacks.getEngine(), 0);
                    double x = bowser.isToRight() ? (104 + 96 - 24) : (-96 + 24);
                    hero.setX(hero.getX() + x);
                    hero.setNumberOfTryToEscape(0);
                    BowserLogic.moveNormal(bowser, bowser.isToRight());
                    bowser.setGrabAttackOn(false);
                    TimerTask followUp = new TimerTask() {
                        @Override
                        public void run() {
                            bowser.setCanHurt(true);
                            bowser.setCoolDownFinished(true);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(followUp, 4000);
                }
            }
        };
        Timer grabTimer = new Timer();
        grabTimer.schedule(task, 5000);
        callbacks.setGrabTimer(grabTimer);
    }
}
