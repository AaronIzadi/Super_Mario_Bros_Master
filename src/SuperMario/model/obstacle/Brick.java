package SuperMario.model.obstacle;

import SuperMario.model.prize.Prize;

import java.awt.image.BufferedImage;

public class Brick extends Obstacle {

    private long timer;
    private long start, finish;

    public Brick(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    public Prize getPrize() {
        return null;
    }

    public void setTimer(long timer) {
        this.timer = timer;
        start = 0;
        finish = 0;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public long getTimer() {
        return timer;
    }
}
