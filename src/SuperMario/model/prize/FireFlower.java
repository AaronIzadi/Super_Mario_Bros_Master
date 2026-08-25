package SuperMario.model.prize;

import SuperMario.logic.GameEngine;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class FireFlower extends PrizeItems {

    public FireFlower(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(20);
    }

    @Override
    public void updateLocation() {
        PrizeLogic.update(this);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        PrizeLogic.onTouch(this, hero, engine);
    }
}
