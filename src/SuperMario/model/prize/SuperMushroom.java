package SuperMario.model.prize;

import SuperMario.logic.GameEngine;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class SuperMushroom extends PrizeItems {

    public SuperMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(30);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        PrizeLogic.onTouch(this, hero, engine);
    }
}
