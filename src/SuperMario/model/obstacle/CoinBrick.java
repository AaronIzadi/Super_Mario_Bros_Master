package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.GameEngine;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.model.prize.Prize;

import java.awt.*;
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

    @Override
    public Prize reveal(GameEngine engine) {
        return BrickLogic.reveal(this, engine);
    }

    @Override
    public void draw(Graphics g) {
        BrickLogic.draw(this, g);
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
    public void animate() {
        BrickLogic.animate(this);
    }

    @Override
    public Prize getPrize() {
        return prize;
    }
}
