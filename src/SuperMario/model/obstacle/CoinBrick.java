package SuperMario.model.obstacle;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.logic.MapManager;
import SuperMario.model.prize.Prize;

import java.awt.image.BufferedImage;

public class CoinBrick extends SurpriseBrick{

    private Prize prize;
    private Animation animation;
    private boolean breaking;
    private int frames;

    public CoinBrick(double x, double y, BufferedImage style, Prize prize) {
        super(x, y, style, prize);
        setBreakable(false);
        setEmpty(false);
        this.prize = prize;
        setAnimation();
        breaking = false;
        frames = animation.getLength();
    }

    private void setAnimation() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        BufferedImage[] frames = imageLoader.getBrickFrames();

        animation = new Animation(frames);
    }

    @Override
    public Prize reveal(GameEngine engine) {
        if (prize != null) {
            prize.reveal();

            setEmpty(true);
            setBreakable(true);
            Prize toReturn = this.prize;
            this.prize = null;
            return toReturn;
        }else{
            MapManager manager = engine.getMapManager();
            if (!manager.getHero().isSuper())
                return null;

            breaking = true;
            manager.addRevealedBrick(this);
            engine.playBreakBrick();

            double newX = getX() - 27, newY = getY() - 27;
            setLocation(newX, newY);

            return null;
        }
    }

    public int getFrames() {
        return frames;
    }

    public void animate() {
        boolean isAnimationTicked = animation.animate(30);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
            frames--;
        }
    }

    @Override
    public Prize getPrize() {
        return prize;
    }
}
