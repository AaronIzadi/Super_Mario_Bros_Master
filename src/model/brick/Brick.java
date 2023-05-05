package model.brick;

import graphic.manager.GameEngine;
import model.GameObj;
import model.prize.Prize;

import java.awt.image.BufferedImage;

public abstract class Brick extends GameObj {

    private BrickType type = BrickType.GROUND_BRICK ;
    private boolean breakable;

    private boolean empty;

    public Brick(double x, double y, BufferedImage style) {
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

    public Prize reveal(GameEngine engine) {
        return null;
    }

    public Prize getPrize() {
        return null;
    }

    public void setType(BrickType type) {
        this.type = type;
    }

    public BrickType getType() {
        return type;
    }
}
