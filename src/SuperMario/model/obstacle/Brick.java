package SuperMario.model.obstacle;

import SuperMario.logic.GameEngine;
import SuperMario.model.GameObject;
import SuperMario.model.hero.Hero;
import SuperMario.model.prize.Prize;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Brick extends GameObject {
    private boolean breakable;
    private boolean empty;
    private long timer;
    private long start, finish;

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

    public boolean isTimeToBreak(Hero hero) {

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

        if (hero.getY() >= 720 + (2 * 48) + hero.getDimension().height) {
            timer = 0;
            start = 0;
            finish = 0;
        }

        return timer >= 2000;
    }

}