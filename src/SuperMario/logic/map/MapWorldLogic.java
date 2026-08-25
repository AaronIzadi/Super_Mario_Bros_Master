package SuperMario.logic.map;

import SuperMario.logic.brick.BrickLogic;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.logic.map.FlagLogic;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.model.enemy.Goomba;
import SuperMario.model.enemy.KoopaTroopa;
import SuperMario.model.enemy.Piranha;
import SuperMario.model.enemy.Spiny;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.enemy.bowser.Fire;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.FireFlower;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.PrizeItems;
import SuperMario.model.prize.SuperStar;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;
import java.util.Iterator;

public final class MapWorldLogic {

    private MapWorldLogic() {
    }

    public static void drawMap(Map map, Graphics2D g2) {
        drawBackground(map, g2);
        drawPrizes(map, g2);

        Bowser bowser = map.getBowser();
        if (bowser != null) {

            BowserLogic.attack(bowser);
            drawBowserFire(map, g2);

            if (bowser.getHp() <= 10) {
                for (Obstacle border : map.getGroundBricks()) {
                    if (border instanceof LavaBorder) {
                        ((LavaBorder) border).setBurn(true);
                    }
                }
                map.getObstacles().clear();
            }

            bowser.getBomb().removeIf(Bomb::isTimeToVanish);
            for (Bomb bomb : bowser.getBomb()) {
                BowserLogic.draw(bomb, g2);
            }

        }

        drawEnemies(map, g2);
        drawBricks(map, g2);
        drawFireballs(map, g2);
        if (map.getCastle() != null) {
            EntityRenderer.draw(map.getCastle(), g2);
        }
        if (map.getEndPoint() != null) {
            EntityRenderer.draw(map.getEndPoint(), g2);
        }
        drawHero(map, g2);
    }

    public static void drawCrossover(Map map, Graphics2D g2) {
        drawBricks(map, g2);
        drawPrizes(map, g2);
        drawHero(map, g2);
    }

    public static void updateLocations(Map map) {
        if (!map.getHero().isGrabbed()) {
            map.getHero().updateLocation();
        }

        Bowser bowser = map.getBowser();
        if (bowser != null) {

            bowser.setToRight(map.getHero().getX() > bowser.getX());

            if (map.getHero().isGrabbed()) {
                map.getHero().setVelY(0);
                map.getHero().setVelX(0);
                map.getHero().setY(bowser.getY() + 48);
                double x = bowser.isToRight() ? (bowser.getX() + 104 - 24) : (bowser.getX() - 24);
                map.getHero().setX(x);
            }

            for (Fire fire : bowser.getFire()) {
                BowserLogic.update(fire);
            }
            for (Bomb bomb : bowser.getBomb()) {
                BowserLogic.update(bomb);
            }
        }

        for (Enemy enemy : map.getEnemies()) {
            updateEnemy(enemy);
        }

        updatePrizeLocation(map);

        Axe axe = map.getAxe();
        if (axe != null && axe.isReleased()) {
            axe.updateLocation();
        }

        for (Fireball fireball : map.getFireballs()) {
            fireball.updateLocation();
        }

        for (Iterator<Brick> brickIterator = map.getRevealedBricks().iterator(); brickIterator.hasNext(); ) {
            Brick brick = brickIterator.next();
            BrickLogic.animate(brick);

            if (brick instanceof CoinBrick) {
                CoinBrick coinBrick = (CoinBrick) brick;
                if (coinBrick.getFrames() < 0) {
                    map.getObstacles().remove(brick);
                    map.getHero().acquirePoints(1);
                    brickIterator.remove();
                }
            } else if (brick instanceof OrdinaryBrick) {
                OrdinaryBrick ordinaryBrick = (OrdinaryBrick) brick;
                if (ordinaryBrick.getFrames() < 0) {
                    map.getObstacles().remove(brick);
                    map.getHero().acquirePoints(1);
                    brickIterator.remove();
                }
            }
        }
        if (map.getEndPoint() != null) {
            FlagLogic.updateLocation(map.getEndPoint());
        }
    }

    public static void updateLocationsForCrossover(Map map) {
        map.getHero().updateLocation();
        updatePrizeLocation(map);

        Axe axe = map.getAxe();
        if (axe != null && axe.isReleased()) {
            axe.updateLocation();
        }
    }

    public static void stopBurning(Map map) {
        for (Obstacle border : map.getGroundBricks()) {
            if (border instanceof LavaBorder) {
                ((LavaBorder) border).setBurn(false);
            }
        }
    }

    private static void updateEnemy(Enemy enemy) {
        if (enemy instanceof Goomba) {
            EnemyLogic.update((Goomba) enemy);
        } else if (enemy instanceof KoopaTroopa) {
            EnemyLogic.update((KoopaTroopa) enemy);
        } else if (enemy instanceof Piranha) {
            EnemyLogic.update((Piranha) enemy);
        } else if (enemy instanceof Spiny) {
            EnemyLogic.update((Spiny) enemy);
        } else if (enemy instanceof Bowser) {
            BowserLogic.update((Bowser) enemy);
        } else {
            enemy.updateLocation();
        }
    }

    private static void drawFireballs(Map map, Graphics2D g2) {
        for (Fireball fireball : map.getFireballs()) {
            EntityRenderer.draw(fireball, g2);
        }
    }

    private static void drawBowserFire(Map map, Graphics2D g2) {
        for (Fire fire : map.getBowser().getFire()) {
            EntityRenderer.draw(fire, g2);
        }
    }

    private static void drawPrizes(Map map, Graphics2D g2) {
        for (Prize prize : map.getRevealedPrizes()) {
            if (prize instanceof Coin) {
                PrizeLogic.draw((Coin) prize, g2);
            } else if (prize instanceof PrizeItems) {
                PrizeLogic.draw((PrizeItems) prize, g2);
            }
        }
    }

    private static void drawBackground(Map map, Graphics2D g2) {
        g2.drawImage(map.getBackgroundImage(), 0, 0, null);
    }

    private static void drawBricks(Map map, Graphics2D g2) {
        for (Obstacle obstacle : map.getObstacles()) {
            if (obstacle != null) {
                BrickLogic.draw(obstacle, g2);
            }
        }

        for (Obstacle obstacle : map.getGroundBricks()) {
            BrickLogic.draw(obstacle, g2);
        }
    }

    private static void drawEnemies(Map map, Graphics2D g2) {
        for (Enemy enemy : map.getEnemies()) {
            if (enemy != null) {
                enemy.draw(g2);
            }
        }
    }

    private static void drawHero(Map map, Graphics2D g2) {
        map.getHero().draw(g2);
    }

    private static void updatePrizeLocation(Map map) {
        for (Iterator<Prize> prizeIterator = map.getRevealedPrizes().iterator(); prizeIterator.hasNext(); ) {
            Prize prize = prizeIterator.next();
            if (prize instanceof Coin) {
                Coin coin = (Coin) prize;
                PrizeLogic.update(coin);
                if (coin.getRevealBoundary() > coin.getY()) {
                    prizeIterator.remove();
                }
            } else if (prize instanceof SuperStar) {
                PrizeLogic.update((SuperStar) prize);
            } else if (prize instanceof FireFlower) {
                PrizeLogic.update((FireFlower) prize);
            } else if (prize instanceof PrizeItems) {
                PrizeLogic.update((PrizeItems) prize);
            }
        }
    }
}
