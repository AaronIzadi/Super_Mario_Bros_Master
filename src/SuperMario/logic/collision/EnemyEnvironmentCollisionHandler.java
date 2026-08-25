package SuperMario.logic.collision;

import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.enemy.Piranha;
import SuperMario.model.enemy.Spiny;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.Obstacle;

import java.awt.*;
import java.util.ArrayList;

final class EnemyEnvironmentCollisionHandler {

    private EnemyEnvironmentCollisionHandler() {
    }

    static void checkEnemyCollisions(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();
        ArrayList<Enemy> enemies = getEnemies(ctx);

        for (Enemy enemy : enemies) {
            if (!(enemy instanceof Piranha)) {
                boolean standsOnBrick = false;

                for (Obstacle obstacle : obstacles) {
                    Rectangle enemyBounds = enemy.getLeftBounds();
                    Rectangle obstacleRightBounds = obstacle.getRightBounds();
                    Rectangle enemyBottomBounds = enemy.getBottomBounds();
                    Rectangle obstacleTopBounds = obstacle.getTopBounds();

                    if (enemy.getVelX() > 0) {
                        enemyBounds = enemy.getRightBounds();
                        obstacleRightBounds = obstacle.getLeftBounds();
                    }

                    if (enemyBounds.intersects(obstacleRightBounds)) {
                        enemy.setVelX(-enemy.getVelX());
                    }

                    if (enemyBottomBounds.intersects(obstacleTopBounds)) {
                        enemy.setFalling(false);
                        enemy.setVelY(0);
                        enemy.setY(obstacle.getY() - enemy.getDimension().height);
                        standsOnBrick = true;
                    }
                }

                if (enemy.getY() + enemy.getDimension().height >= callbacks.getMap().getBottomBorder()) {
                    enemy.setFalling(false);
                    enemy.setVelY(0);
                }

                if (!standsOnBrick && enemy.getY() < callbacks.getMap().getBottomBorder()) {
                    enemy.setFalling(true);
                }
            }
        }
    }

    private static ArrayList<Enemy> getEnemies(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        ArrayList<Enemy> enemies = currentMap.getEnemies();
        Hero hero = callbacks.getHero();

        for (Enemy enemy : enemies) {
            if (enemy instanceof Spiny
                    && ((hero.getY() + hero.getStyle().getHeight()) == (enemy.getY() + enemy.getStyle().getHeight() + 1))) {
                Spiny spiny = (Spiny) enemy;
                if (Math.abs(spiny.getX() - hero.getX()) <= 192) {
                    EnemyLogic.moveFaster(spiny);
                } else {
                    EnemyLogic.moveNormal(spiny);
                }
            }
        }
        return enemies;
    }
}
