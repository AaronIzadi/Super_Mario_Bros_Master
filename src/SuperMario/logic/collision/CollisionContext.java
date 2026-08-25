package SuperMario.logic.collision;

import SuperMario.model.GameObject;

import java.util.ArrayList;

public final class CollisionContext {

    private final MapCollisionCallbacks callbacks;
    private final ArrayList<GameObject> toBeRemoved = new ArrayList<>();

    public CollisionContext(MapCollisionCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public MapCollisionCallbacks callbacks() {
        return callbacks;
    }

    public ArrayList<GameObject> toBeRemoved() {
        return toBeRemoved;
    }
}
