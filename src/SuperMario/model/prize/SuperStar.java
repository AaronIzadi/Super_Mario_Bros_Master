package SuperMario.model.prize;

import SuperMario.logic.GameEngine;
import SuperMario.logic.prize.PrizeLogic;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;

public class SuperStar extends PrizeItems {

    private boolean isJumpTimerActivated = false;

    public SuperStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
        setVelX(2);
    }

    @Override
    public void updateLocation() {
        PrizeLogic.update(this);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        PrizeLogic.onTouch(this, hero, engine);
    }

    public boolean isJumpTimerActivated() {
        return isJumpTimerActivated;
    }

    public void setJumpTimerActivated(boolean jumpTimerActivated) {
        isJumpTimerActivated = jumpTimerActivated;
    }
}
