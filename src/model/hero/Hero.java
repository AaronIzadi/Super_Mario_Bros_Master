package model.hero;

import graphic.manager.Camera;
import logic.GameEngine;
import graphic.view.Animation;
import model.GameObject;
import graphic.view.ImageLoader;
import model.prize.Fireball;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Hero extends GameObject {
    private int remainingLives;
    private int coins;
    private int points;
    private double invincibilityTimer;
    private HeroForm heroForm;
    private int type;
    private boolean toRight = true;
    private String mapPath;
    private boolean tookStar;

    public Hero(double x, double y) {
        super(x, y, null);
        setDimension(48, 48);

        remainingLives = 3;
        points = 0;
        coins = 0;
        invincibilityTimer = 0;

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.setHeroType(type);
        BufferedImage[] leftFrames = imageLoader.getLeftFrames(HeroForm.SMALL);
        BufferedImage[] rightFrames = imageLoader.getRightFrames(HeroForm.SMALL);

        Animation animation = new Animation(leftFrames, rightFrames);
        heroForm = new HeroForm(animation, false, false, type);
        setStyle(heroForm.getCurrentStyle(toRight, false, false));
    }

    @Override
    public void draw(Graphics g) {
        boolean movingInX = (getVelX() != 0);
        boolean movingInY = (getVelY() != 0);

        setStyle(heroForm.getCurrentStyle(toRight, movingInX, movingInY));

        super.draw(g);
    }

    public void jump(GameEngine engine) {
        if (!isJumping() && !isFalling()) {
            setJumping(true);
            setVelY(10);
            engine.playJump();
        }
    }

    public void move(boolean toRight, Camera camera) {
        if (toRight) {
            setVelX(5);
        } else if (camera.getX() < getX()) {
            setVelX(-5);
        }

        this.toRight = toRight;
    }

    public void setTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setTookStar(false);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 15000);
    }

    public void setTimerToRun(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setTookStar(false);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }
    public boolean onTouchEnemy(GameEngine engine) {

        if (!ifTookStar()){
            if (!heroForm.isSuper() && !heroForm.ifCanShootFire()) {
                remainingLives--;
                points = points > 20 ? points - 20 : 0;
                if (engine.getUserData().getHero() != null) {
                    engine.getUserData().getHero().setRemainingLives(engine.getUserData().getHero().getRemainingLives() - 1);
                    engine.getUserData().getHero().setPoints(engine.getUserData().getHero().points > 20 ? engine.getUserData().getHero().points - 20 : 0);

                }
                engine.playHeroDies();
                return true;
            } else {
                engine.shakeCamera();
                heroForm = heroForm.onTouchEnemy(engine.getImageLoader());
                if (engine.getUserData().getHero() != null) {
                    engine.getUserData().getHero().setHeroForm(engine.getUserData().getHero().getHeroForm().onTouchEnemy(engine.getImageLoader()));
                }
                setDimension(48, 48);
                return false;
            }
        }
        return false;
    }

    public void onTouchHole(GameEngine engine) {
        remainingLives = remainingLives - 1;
        heroForm = heroForm.onTouchEnemy(engine.getImageLoader());
        points = points > 30 ? points - 30 : 0;
        if (engine.getUserData().getHero() != null) {
            engine.getUserData().getHero().setRemainingLives(engine.getUserData().getHero().getRemainingLives() - 1);
            engine.getUserData().getHero().setHeroForm(engine.getUserData().getHero().getHeroForm().onTouchEnemy(engine.getImageLoader()));
            engine.getUserData().getHero().setPoints(engine.getUserData().getHero().points > 30 ? engine.getUserData().getHero().points - 30 : 0);
        }
        engine.playHeroDies();
        setDimension(48, 48);
    }

    public Fireball fire() {
        return heroForm.fire(toRight, getX(), getY());
    }

    public void acquireCoin() {
        coins++;
    }

    public void acquirePoints(int point) {
        points = points + point;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }

    public int getPoints() {
        return points;
    }

    public int getCoins() {
        return coins;
    }

    public HeroForm getHeroForm() {
        return heroForm;
    }

    public void setHeroForm(HeroForm heroForm) {
        this.heroForm = heroForm;
    }

    public boolean isSuper() {
        return heroForm.isSuper();
    }

    public boolean getToRight() {
        return toRight;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public double getInvincibilityTimer() {
        return invincibilityTimer;
    }

    public void setInvincibilityTimer(double invincibilityTimer) {
        this.invincibilityTimer = invincibilityTimer;
    }

    public void setExtraCoin() {
        this.coins++;
    }


    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void resetLocation() {
        setVelX(0);
        setVelY(0);
        setX(50);
        setJumping(false);
        setFalling(true);
    }

    public void jumpForLuigi(GameEngine engine) {
        if (!isJumping() && !isFalling()) {
            setJumping(true);
            setVelY(13);
            engine.playJump();
        }
    }

    public void jumpForPrincePeach(GameEngine engine) {
        if (!isJumping() && !isFalling()) {
            setJumping(true);
            setVelY(12);
            engine.playJump();
        }
    }

    public void moveForPrincePeach(boolean toRight, Camera camera) {
        if (toRight) {
            setVelX(6);
        } else if (camera.getX() < getX()) {
            setVelX(-6);
        }

        this.toRight = toRight;
    }

    public void moveForRoss(boolean toRight, Camera camera) {
        if (toRight) {
            setVelX(7);
        } else if (camera.getX() < getX()) {
            setVelX(-7);
        }

        this.toRight = toRight;
    }

    @Override
    public BufferedImage getStyle() {
        return super.getStyle();
    }

    public void acquireCoinForToad() {
        setExtraCoin();
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setTookStar(boolean tookStar) {
        this.tookStar = tookStar;
    }

    public boolean ifTookStar() {
        return tookStar;
    }
}
