package SuperMario.logic.collision;

import SuperMario.logic.GameEngine;
import SuperMario.model.enemy.bowser.Bomb;

public final class CollisionCoordinator {

    private CollisionCoordinator() {
    }

    public static void checkCollisions(CollisionContext ctx, GameEngine engine) {
        MapCollisionCallbacks callbacks = ctx.callbacks();
        if (callbacks.getMap() == null) {
            return;
        }

        HeroObstacleCollisionHandler.checkBottomCollisions(ctx);
        HeroObstacleCollisionHandler.checkTopCollisions(ctx);
        HeroObstacleCollisionHandler.checkHeroHorizontalCollision(ctx);
        EnemyEnvironmentCollisionHandler.checkEnemyCollisions(ctx);
        PrizeCollisionHandler.checkPrizeCollision(ctx);
        PrizeCollisionHandler.checkPrizeContact(ctx);
        HeroWeaponCollisionHandler.checkWeaponContact(ctx);

        if (callbacks.getMap().getBowser() != null) {
            BowserCollisionHandler.checkEnemyWeaponContact(ctx);
            BowserCollisionHandler.checkBowserPossibleCollisions(ctx, callbacks.getMap().getBowser());
            for (Bomb bomb : callbacks.getMap().getBowser().getBomb()) {
                BowserCollisionHandler.checkBowserPossibleCollisions(ctx, bomb);
            }
            if (callbacks.getHero().isGrabbed()) {
                BowserCollisionHandler.updateGrabState(ctx);
            }
        }
    }
}
