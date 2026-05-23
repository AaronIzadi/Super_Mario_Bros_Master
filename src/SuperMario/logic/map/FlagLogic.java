package SuperMario.logic.map;

import SuperMario.logic.physics.Physics;
import SuperMario.model.map.Flag;

public final class FlagLogic {

    private FlagLogic() {
    }

    public static void updateLocation(Flag flag) {
        if (flag.isTouched()) {
            if (flag.getY() + flag.getDimension().getHeight() >= 576) {
                flag.setFalling(false);
                flag.setVelY(0);
                flag.setY(576 - flag.getDimension().getHeight());
            }
            Physics.updateLocation(flag);
        }
    }
}
