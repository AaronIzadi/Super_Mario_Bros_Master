package SuperMario.model.prize;


import SuperMario.logic.GameEngine;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class HeartMushroom extends PrizeItems {

    public HeartMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(50);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        hero.acquirePoints(getPoint());
        hero.setRemainingLives(hero.getRemainingLives() + 1);
        engine.playOneUp();
    }
}
