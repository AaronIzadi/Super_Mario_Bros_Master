package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;
import model.hero.HeroForm;
import graphic.view.Animation;
import graphic.view.ImageLoader;

import java.awt.image.BufferedImage;

public class MagicFlower extends PrizeItems {

    public MagicFlower(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(20);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
    }

    @Override
    public void updateLocation() {
    }

}
