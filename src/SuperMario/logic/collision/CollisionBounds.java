package SuperMario.logic.collision;

import SuperMario.model.GameObject;

import java.awt.*;

public final class CollisionBounds {

    private CollisionBounds() {
    }

    public static Rectangle getTopBounds(GameObject object) {
        Dimension dimension = object.getDimension();
        return new Rectangle((int) object.getX() + dimension.width / 6, (int) object.getY(),
                2 * dimension.width / 3, dimension.height / 2);
    }

    public static Rectangle getBottomBounds(GameObject object) {
        Dimension dimension = object.getDimension();
        return new Rectangle((int) object.getX() + dimension.width / 6, (int) object.getY() + dimension.height / 2,
                2 * dimension.width / 3, dimension.height / 2);
    }

    public static Rectangle getLeftBounds(GameObject object) {
        Dimension dimension = object.getDimension();
        return new Rectangle((int) object.getX(), (int) object.getY() + dimension.height / 4,
                dimension.width / 4, dimension.height / 2);
    }

    public static Rectangle getRightBounds(GameObject object) {
        Dimension dimension = object.getDimension();
        return new Rectangle((int) object.getX() + 3 * dimension.width / 4, (int) object.getY() + dimension.height / 4,
                dimension.width / 4, dimension.height / 2);
    }

    public static Rectangle getBounds(GameObject object) {
        Dimension dimension = object.getDimension();
        return new Rectangle((int) object.getX(), (int) object.getY(), dimension.width, dimension.height);
    }
}
