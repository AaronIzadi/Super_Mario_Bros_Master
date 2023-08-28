package SuperMario.model.obstacle;

import SuperMario.logic.GameEngine;
import SuperMario.model.GameObject;
import SuperMario.model.prize.Prize;

import java.awt.image.BufferedImage;

public class Brick extends Obstacle {

    private long timer;
    private long start, finish;

    public Brick(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    @Override
    public boolean isBreakable() {
        return super.isBreakable();
    }

    @Override
    public void setBreakable(boolean breakable) {
        super.setBreakable(breakable);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public void setEmpty(boolean empty) {
        super.setEmpty(empty);
    }

    public Prize reveal(GameEngine engine) {
        return null;
    }

    public Prize getPrize() {
        return null;
    }

    public boolean isTimeToBreak() {

        if (start == 0) {
            start = System.currentTimeMillis();
        } else {
            finish = System.currentTimeMillis();
            timer = finish - start;
            if (timer >= 2000) {
                return true;
            }
            finish = 0;
        }

        return timer >= 2000;
    }

    public void setTimer(long timer) {
        this.timer = timer;
        start = 0;
        finish = 0;
    }
}