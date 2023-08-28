package SuperMario.model.obstacle;

import SuperMario.model.GameObject;

import java.awt.image.BufferedImage;

public abstract class Obstacle extends GameObject {

    private boolean breakable;
    private boolean empty;

    public Obstacle(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

}
