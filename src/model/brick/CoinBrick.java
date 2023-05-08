package model.brick;

import graphic.manager.GameEngine;
import graphic.manager.MapManager;
import graphic.view.Animation;
import graphic.view.ImageLoader;
import model.hero.HeroType;
import model.prize.Prize;

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
        frames = animation.getLeftFrames().length;
    }

    private void setAnimation() {
        ImageLoader imageLoader = new ImageLoader(HeroType.MARIO);
        BufferedImage[] leftFrames = imageLoader.getBrickFrames();

        animation = new Animation(leftFrames, leftFrames);
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

            double newX = getX() - 27, newY = getY() - 27;
            setLocation(newX, newY);

            return null;
        }
    }

    public int getFrames() {
        return frames;
    }

    public void animate() {
        if (breaking) {
            setStyle(animation.animate(3, true));
            frames--;
        }
    }

    @Override
    public Prize getPrize() {
        return prize;
    }
}
