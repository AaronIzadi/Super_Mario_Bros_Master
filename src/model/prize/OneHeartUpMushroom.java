package model.prize;

import logic.GameEngine;
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
        if (engine.getUserData().getHero() != null) {
            engine.getUserData().getHero().acquirePoints(getPoint());
            engine.getUserData().getHero().setRemainingLives(engine.getUserData().getHero().getRemainingLives() + 1);
        }
        engine.playOneUp();
    }
}
