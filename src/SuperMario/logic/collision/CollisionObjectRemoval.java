package SuperMario.logic.collision;

import SuperMario.logic.hero.HeroLogic;
import SuperMario.model.GameObject;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.enemy.bowser.Bomb;
import SuperMario.model.enemy.bowser.Fire;
import SuperMario.model.map.Map;
import SuperMario.model.obstacle.Brick;
import SuperMario.model.prize.Coin;
import SuperMario.model.prize.Prize;
import SuperMario.model.prize.PrizeItems;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.util.ArrayList;

final class CollisionObjectRemoval {

    private CollisionObjectRemoval() {
    }

    static void removeObjects(CollisionContext ctx, ArrayList<GameObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        MapCollisionCallbacks callbacks = ctx.callbacks();
        Map currentMap = callbacks.getActiveMap();

        for (GameObject object : list) {
            if (object instanceof Fireball) {
                currentMap.removeFireball((Fireball) object);
            } else if (object instanceof Enemy) {
                currentMap.removeEnemy((Enemy) object);
            } else if (object instanceof Coin || object instanceof PrizeItems) {
                currentMap.removePrize((Prize) object);
            } else if (object instanceof Brick) {
                currentMap.removeObstacle((Brick) object);
            } else if (object instanceof Fire) {
                if (callbacks.getMap().getBowser() != null) {
                    currentMap.getBowser().getFire().remove(object);
                }
            } else if (object instanceof Bomb) {
                if (callbacks.getMap().getBowser() != null) {
                    currentMap.getBowser().getBomb().remove(object);
                }
            } else if (object instanceof Axe) {
                HeroLogic.deactivateAxe(callbacks.getHero());
            }
        }
    }
}
