package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;

import java.awt.image.BufferedImage;

public class MagicStar extends PrizeItems{

    public MagicStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        super.onTouch(hero,engine);
    }

    @Override
    public void reveal() {
        super.reveal();
    }
}
