package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;

import java.awt.image.BufferedImage;

public class OrdinaryMushroom extends PrizeItems {

    public OrdinaryMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(200);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        hero.acquirePoints(getPoint());
        if (engine.getHero() != null) {
            engine.getHero().acquirePoints(getPoint());
        }
        hero.setRemainingLives(hero.getRemainingLives() + 1);
        if (engine.getHero() != null) {
            engine.getHero().setRemainingLives(engine.getHero().getRemainingLives() + 1);
        }
        engine.playOneUp();
    }
}
