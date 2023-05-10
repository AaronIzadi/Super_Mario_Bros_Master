package model.prize;

import logic.GameEngine;
import model.hero.Hero;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class SuperStar extends PrizeItems {


    public SuperStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
        setVelX(2);
    }

    @Override
    public void updateLocation() {
        super.updateLocation();
        setTimerToJump();
        if (Math.floor(getY()) == (720 - 95 - 48)) {
            if (!isJumping() && !isFalling()) {
                setJumping(true);
                setY(getY() - 48);
            }
        }
    }

    public void setTimerToJump() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setJumping(false);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }

    @Override
    public void onTouch(Hero hero, GameEngine engine) {
        super.onTouch(hero, engine);
        hero.setTookStar(true);
        hero.setTimer();
    }

}
