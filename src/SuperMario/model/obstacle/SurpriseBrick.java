package SuperMario.model.obstacle;


import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.GameEngine;
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

        BufferedImage newStyle = engine.getImageLoader().getRevealedPrizeBrick();

        if (prize != null) {
            prize.reveal();
        }

        setEmpty(true);
        setStyle(newStyle);

        Prize toReturn = this.prize;
        this.prize = null;
        return toReturn;
    }

    @Override
    public Prize getPrize() {
        return prize;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if (!isEmpty()){
            animate();
        }
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(5);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
    }

    public void setFrames(BufferedImage[] frames) {
        setAnimation(new Animation(frames));
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
