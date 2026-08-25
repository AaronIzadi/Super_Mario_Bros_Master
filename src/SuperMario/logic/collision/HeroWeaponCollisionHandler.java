package SuperMario.logic.collision;

import SuperMario.config.GameConstants;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.model.GameObject;
import SuperMario.model.enemy.*;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.GroundBrick;
import SuperMario.model.obstacle.Obstacle;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;
import java.util.ArrayList;

final class HeroWeaponCollisionHandler {

    private HeroWeaponCollisionHandler() {
    }

    static void checkWeaponContact(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        ArrayList<Fireball> fireballs = currentMap.getFireballs();
        Axe axe = callbacks.getMap().getHero().getAxe();

        if (axe != null) {
            checkWeaponCollision(ctx, axe);
        }

        for (Fireball fireball : fireballs) {
            checkWeaponCollision(ctx, fireball);
        }

        CollisionObjectRemoval.removeObjects(ctx, ctx.toBeRemoved());
    }

    private static void checkWeaponCollision(CollisionContext ctx, GameObject object) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        ArrayList<Enemy> enemies = currentMap.getEnemies();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<GameObject> toBeRemoved = ctx.toBeRemoved();
        Hero hero = callbacks.getHero();

        Rectangle objectBounds = object.getBounds();
        Bowser bowser = currentMap.getBowser();

        if (object instanceof Fireball && bowser != null) {
            double distance;
            if (object.isToRight() && !bowser.isToRight()) {
                distance = bowser.getX() - object.getX();
            } else if (!object.isToRight() && bowser.isToRight()) {
                distance = object.getX() - bowser.getX();
            } else {
                distance = 0;
            }

            if (distance <= (2 * GameConstants.TILE_SIZE) && distance >= GameConstants.TILE_SIZE) {
                BowserLogic.jump(bowser);
            }
        }

        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = enemy.getBounds();
            if (objectBounds.intersects(enemyBounds)) {
                if (enemy instanceof Bowser) {
                    BowserLogic.setHp((Bowser) enemy, ((Bowser) enemy).getHp() - 1);
                    if (callbacks.checkIfBowserDies()) {
                        toBeRemoved.add(enemy);
                        callbacks.getMap().setBowser(null);
                        callbacks.getEngine().getSoundManager().heroWinsOverBowser();
                    }
                } else if (enemy instanceof Goomba) {
                    callbacks.acquirePoints(1);
                    toBeRemoved.add(enemy);
                } else if (enemy instanceof KoopaTroopa) {
                    KoopaTroopa koopaTroopa = (KoopaTroopa) enemy;
                    if (!koopaTroopa.isHit()) {
                        koopaTroopa.setHit(true);
                        EnemyLogic.moveAfterHit(koopaTroopa);
                    } else {
                        callbacks.acquirePoints(2);
                        toBeRemoved.add(enemy);
                    }
                } else if (enemy instanceof Spiny) {
                    callbacks.acquirePoints(3);
                    toBeRemoved.add(enemy);
                } else {
                    callbacks.acquirePoints(1);
                    toBeRemoved.add(enemy);
                }
                callbacks.getEngine().getSoundManager().playKickEnemy();
                toBeRemoved.add(object);
            }
        }

        if (object instanceof Fireball || (object instanceof Axe && hero.getAxe().isReleased())) {
            Axe thrownAxe = object instanceof Axe ? (Axe) object : null;
            for (Obstacle obstacle : obstacles) {
                if (obstacle instanceof GroundBrick) {
                    continue;
                }
                if (thrownAxe != null
                        && Math.abs(thrownAxe.getX() - thrownAxe.getXReleasePoint()) < GameConstants.AXE_MIN_TRAVEL_BEFORE_BLOCK) {
                    continue;
                }
                Rectangle obstacleBounds = obstacle.getBounds();
                if (objectBounds.intersects(obstacleBounds)) {
                    toBeRemoved.add(object);
                }
            }
        }
    }
}
