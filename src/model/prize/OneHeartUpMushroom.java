package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;

import java.awt.image.BufferedImage;

public class OneHeartUpMushroom extends PrizeItems {

    public OneHeartUpMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(50);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        hero.acquirePoints(getPoint());
        hero.setRemainingLives(hero.getRemainingLives() + 1);
        if (engine.getHero() != null) {
            engine.getHero().acquirePoints(getPoint());
            engine.getHero().setRemainingLives(engine.getHero().getRemainingLives() + 1);
        }
        engine.playOneUp();
    }
}
