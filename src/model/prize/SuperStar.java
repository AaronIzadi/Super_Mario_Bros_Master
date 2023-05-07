package model.prize;

import graphic.manager.GameEngine;
import model.hero.Hero;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class SuperStar extends PrizeItems {

    Hero hero;

    public SuperStar(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(40);
    }

    @Override
    public void updateLocation() {

        if (isJumping() && getVelY() <= 0) {
            setJumping(false);
            setFalling(true);
        } else if (isJumping()) {
            setVelY(getVelY() - getGravityAcc());
            setY(getY() - getVelY());
        }

        if (isFalling()) {
            setY(getY() + getVelY());
            setVelY(getVelY() + getGravityAcc());
        }

        setX(getX() + getVelX());


        if (Math.floor(getY()) == (720 - 95 - 48)) {
            setTimer();
        }

    }

    public void setTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setJumping(true);
                setVelY(5);
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

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
