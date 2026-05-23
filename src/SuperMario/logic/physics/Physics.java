package SuperMario.logic.physics;

import SuperMario.model.GameObject;

public final class Physics {

    private Physics() {
    }

    public static void updateLocation(GameObject object) {
        if (object.isJumping() && object.getVelY() <= 0) {
            object.setJumping(false);
            object.setFalling(true);
        }

        if (object.isFalling() || object.isJumping()) {
            object.setVelY(object.getVelY() - object.getGravityAcc());
        }
        object.setY(object.getY() - object.getVelY());
        object.setX(object.getX() + object.getVelX());
    }
}
