package SuperMario.model.prize;


import SuperMario.logic.GameEngine;
import SuperMario.model.hero.Hero;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class SuperStar extends PrizeItems {

    private boolean isJumpTimerActivated = false;

    public SuperStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
        setVelX(2);
    }

    @Override
    public void updateLocation() {
        super.updateLocation();
        setTimerToJump();
    }

    public void setTimerToJump() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (Math.floor(getY()) == (720 - 96 - 48 + 1) && !isJumping()) {
                    setJumping(true);
                    setVelY(7);
                }
                isJumpTimerActivated = false;
            }
        };

        if (!isJumpTimerActivated && Math.floor(getY()) == (720 - 96 - 48 + 1) && !isJumping() && !isFalling()) {
            isJumpTimerActivated = true;
            Timer timer = new Timer();
            timer.schedule(task, 1000);

        }
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        super.onTouch(hero, engine);
        hero.setTookStar(true);
        engine.playSuperStar();
        hero.setTimer();
    }

}
