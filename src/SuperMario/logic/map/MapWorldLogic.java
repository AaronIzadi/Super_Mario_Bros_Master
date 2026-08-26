package SuperMario.logic.map;

import SuperMario.logic.brick.BrickLogic;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.logic.enemy.EnemyLogic;
import SuperMario.logic.hero.HeroFormLogic;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.prize.PrizeHandler;
import SuperMario.logic.render.EntityRenderer;
import SuperMario.logic.weapon.WeaponLogic;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Bowser;
import SuperMario.model.enemy.bowser.Fire;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.*;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

            bowser.getBomb().removeIf(Bomb::isTimeToVanish);
            for (Bomb bomb : snapshot(bowser.getBomb())) {
                BowserLogic.draw(bomb, g2);
            }
        }

        drawEnemies(map, g2);
        drawBricks(map, g2);
        drawFireballs(map, g2);
        if (map.getCastle() != null) {
            EntityRenderer.drawSprite(map.getCastle(), g2);
        }
        if (map.getEndPoint() != null) {
            EntityRenderer.drawSprite(map.getEndPoint(), g2);
        }
        HeroLogic.draw(map.getHero(), g2);
    }

    public static void drawCrossover(Map map, Graphics2D g2) {
        drawBricks(map, g2);
        drawPrizes(map, g2);
        HeroLogic.draw(map.getHero(), g2);
    }

    public static void updateLocations(Map map) {
        Hero hero = map.getHero();
        if (!hero.isGrabbed()) {
            Physics.updateLocation(hero);
        }
        updateHeroWalkingAnimation(hero);

        Bowser bowser = map.getBowser();
        if (bowser != null) {
            bowser.setToRight(hero.getX() > bowser.getX());
            updateBowserLowHpEffects(map, bowser);

            if (hero.isGrabbed()) {
                hero.setVelY(0);
                hero.setVelX(0);
                hero.setY(bowser.getY() + 48);
                double x = bowser.isToRight() ? (bowser.getX() + 104 - 24) : (bowser.getX() - 24);
                hero.setX(x);
            }

            for (Fire fire : bowser.getFire()) {
                BowserLogic.update(fire);
            }
            for (Bomb bomb : bowser.getBomb()) {
                BowserLogic.update(bomb);
            }
        }

        for (Enemy enemy : map.getEnemies()) {
            EnemyLogic.update(enemy);
        }

        updatePrizeLocation(map);

        Axe axe = map.getAxe();
        if (axe != null && axe.isReleased()) {
            WeaponLogic.update(axe);
        }

        updateFireballs(map);

        for (Iterator<Brick> brickIterator = map.getRevealedBricks().iterator(); brickIterator.hasNext(); ) {
            Brick brick = brickIterator.next();
            BrickLogic.animate(brick);

            if (brick instanceof CoinBrick) {
                CoinBrick coinBrick = (CoinBrick) brick;
                if (coinBrick.getFrames() < 0) {
                    map.getObstacles().remove(brick);
                    HeroLogic.acquirePoints(hero, 1);
                    brickIterator.remove();
                }
            } else if (brick instanceof OrdinaryBrick) {
                OrdinaryBrick ordinaryBrick = (OrdinaryBrick) brick;
                if (ordinaryBrick.getFrames() < 0) {
                    map.getObstacles().remove(brick);
                    HeroLogic.acquirePoints(hero, 1);
                    brickIterator.remove();
                }
            }
        }

        if (map.getEndPoint() != null) {
            FlagLogic.updateLocation(map.getEndPoint());
        }
    }

    public static void updateLocationsForCrossover(Map map) {
        Hero hero = map.getHero();
        Physics.updateLocation(hero);
        updateHeroWalkingAnimation(hero);
        updatePrizeLocation(map);

        Axe axe = map.getAxe();
        if (axe != null && axe.isReleased()) {
            WeaponLogic.update(axe);
        }

        updateFireballs(map);
    }

    private static void updateFireballs(Map map) {
        for (Iterator<Fireball> fireballIterator = map.getFireballs().iterator(); fireballIterator.hasNext(); ) {
            Fireball fireball = fireballIterator.next();
            if (WeaponLogic.update(fireball, map)) {
                fireballIterator.remove();
            }
        }
    }

    private static void updateBowserLowHpEffects(Map map, Bowser bowser) {
        if (bowser.getHp() <= 10) {
            for (Obstacle border : map.getGroundBricks()) {
                if (border instanceof LavaBorder) {
                    ((LavaBorder) border).setBurn(true);
                }
            }
            map.getObstacles().clear();
        }
    }

    public static void stopBurning(Map map) {
        for (Obstacle border : map.getGroundBricks()) {
            if (border instanceof LavaBorder) {
                ((LavaBorder) border).setBurn(false);
            }
        }
    }

    private static void updateHeroWalkingAnimation(Hero hero) {
        if (hero.getVelX() != 0 && hero.getVelY() == 0) {
            HeroFormLogic.animateWalking(hero.getHeroForm(), hero.getToRight());
        }
    }

    private static <T> List<T> snapshot(List<T> items) {
        return new ArrayList<>(items);
    }

    private static void drawFireballs(Map map, Graphics2D g2) {
        for (Fireball fireball : snapshot(map.getFireballs())) {
            EntityRenderer.drawSprite(fireball, g2);
        }
    }

    private static void drawBowserFire(Map map, Graphics2D g2) {
        for (Fire fire : snapshot(map.getBowser().getFire())) {
            EntityRenderer.drawSprite(fire, g2);
        }
    }

    private static void drawPrizes(Map map, Graphics2D g2) {
        for (Prize prize : snapshot(map.getRevealedPrizes())) {
            PrizeHandler.draw(prize, g2);
        }
    }

    private static void drawBackground(Map map, Graphics2D g2) {
        g2.drawImage(map.getBackgroundImage(), 0, 0, null);
    }

    private static void drawBricks(Map map, Graphics2D g2) {
        for (Obstacle obstacle : snapshot(map.getObstacles())) {
            if (obstacle != null) {
                BrickLogic.draw(obstacle, g2);
            }
        }

        for (Obstacle obstacle : snapshot(map.getGroundBricks())) {
            BrickLogic.draw(obstacle, g2);
        }
    }

    private static void drawEnemies(Map map, Graphics2D g2) {
        for (Enemy enemy : snapshot(map.getEnemies())) {
            if (enemy != null) {
                EnemyLogic.draw(enemy, g2);
            }
        }
    }

    private static void updatePrizeLocation(Map map) {
        for (Iterator<Prize> prizeIterator = map.getRevealedPrizes().iterator(); prizeIterator.hasNext(); ) {
            Prize prize = prizeIterator.next();
            PrizeHandler.update(prize);
            if (prize instanceof Coin) {
                Coin coin = (Coin) prize;
                if (coin.getRevealBoundary() > coin.getY()) {
                    prizeIterator.remove();
                }
            }
        }
    }
}
