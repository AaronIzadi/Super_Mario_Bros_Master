package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.GameEngine;
import SuperMario.logic.brick.BrickLogic;
import SuperMario.model.prize.Prize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SurpriseBrick extends Brick {

    private Prize prize;
    private Animation animation;

    public SurpriseBrick(double x, double y, BufferedImage style, Prize prize) {
        super(x, y, style);
        setBreakable(false);
        setEmpty(false);
        this.prize = prize;
    }

    @Override
    public Prize reveal(GameEngine engine) {
        return BrickLogic.reveal(this, engine);
    }

    @Override
    public Prize getPrize() {
        return prize;
    }

    @Override
    public void draw(Graphics g) {
        BrickLogic.draw(this, g);
    }

    public void animate() {
        BrickLogic.animate(this);
    }

    public void setFrames(BufferedImage[] frames) {
        setAnimation(new Animation(frames));
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }
}
