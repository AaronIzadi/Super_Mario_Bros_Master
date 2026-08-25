package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.model.prize.Prize;

import java.awt.image.BufferedImage;

public class CoinBrick extends SurpriseBrick {

    private Prize prize;
    private Animation animation;
    private int frames;

    public CoinBrick(double x, double y, BufferedImage style, Prize prize) {
        super(x, y, style, prize);
        setBreakable(false);
        setEmpty(false);
        this.prize = prize;
        BrickLogic.initializeCoinBrick(this);
    }

    public int getFrames() {
        return frames;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setFrameCount(int frames) {
        this.frames = frames;
    }

    public void decrementFrames() {
        frames--;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    @Override
    public Prize getPrize() {
        return prize;
    }
}
