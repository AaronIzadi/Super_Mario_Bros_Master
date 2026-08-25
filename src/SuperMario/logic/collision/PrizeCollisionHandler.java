package SuperMario.logic.collision;

import SuperMario.logic.prize.PrizeHandler;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.Obstacle;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.PrizeItems;

import java.awt.*;
import java.util.ArrayList;

final class PrizeCollisionHandler {

    private PrizeCollisionHandler() {
    }

    static void checkPrizeCollision(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        ArrayList<Prize> prizes = currentMap.getRevealedPrizes();
        ArrayList<Obstacle> obstacles = currentMap.getAllObstacles();

        for (Prize prize : prizes) {
            if (prize instanceof PrizeItems) {
                PrizeItems boost = (PrizeItems) prize;
                Rectangle prizeBottomBounds = boost.getBottomBounds();
                Rectangle prizeRightBounds = boost.getRightBounds();
                Rectangle prizeLeftBounds = boost.getLeftBounds();
                boost.setFalling(true);

                for (Obstacle obstacle : obstacles) {
                    Rectangle obstacleBounds;

                    if (boost.isFalling()) {
                        obstacleBounds = obstacle.getTopBounds();
                        if (obstacleBounds.intersects(prizeBottomBounds)) {
                            boost.setFalling(false);
                            boost.setVelY(0);
                            boost.setY(obstacle.getY() - boost.getDimension().height + 1);
                            if (boost.getVelX() == 0) {
                                boost.setVelX(2);
                            }
                        }
                    }

                    if (boost.getVelX() > 0) {
                        obstacleBounds = obstacle.getLeftBounds();
                        if (obstacleBounds.intersects(prizeRightBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    } else if (boost.getVelX() < 0) {
                        obstacleBounds = obstacle.getRightBounds();
                        if (obstacleBounds.intersects(prizeLeftBounds)) {
                            boost.setVelX(-boost.getVelX());
                        }
                    }
                }

                if (boost.getY() + boost.getDimension().height > callbacks.getMap().getBottomBorder()) {
                    boost.setFalling(false);
                    boost.setVelY(0);
                    boost.setY(callbacks.getMap().getBottomBorder() - boost.getDimension().height);
                    if (boost.getVelX() == 0) {
                        boost.setVelX(2);
                    }
                }
            }
        }
    }

    static void checkPrizeContact(CollisionContext ctx) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();
        ArrayList<Prize> prizes = currentMap.getRevealedPrizes();
        ArrayList<GameObject> toBeRemoved = ctx.toBeRemoved();
        Hero hero = callbacks.getHero();

        Rectangle heroBounds = hero.getBounds();
        for (Prize prize : prizes) {
            Rectangle prizeBounds = ((GameObject) prize).getBounds();
            if (prizeBounds.intersects(heroBounds)) {
                PrizeHandler.onTouch(prize, hero, callbacks.getEngine());
                toBeRemoved.add((GameObject) prize);
            } else if (prize instanceof Coin) {
                PrizeHandler.onTouch(prize, hero, callbacks.getEngine());
            }
        }

        CollisionObjectRemoval.removeObjects(ctx, toBeRemoved);
    }
}
