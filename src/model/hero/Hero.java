package model.hero;

import graphic.manager.Camera;
import graphic.manager.GameEngine;
import graphic.view.Animation;
import model.GameObject;
import graphic.view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Hero extends GameObject {
    private int remainingLives;
    private int coins;
    private int points;
    private double invincibilityTimer;
    private HeroForm heroForm;
    private int type = HeroType.MARIO;
    private boolean toRight = true;
    private List<Integer> typesOwned = new LinkedList<>();

    private String mapPath;

    public Hero(double x, double y) {
        super(x, y, null);
        setDimension(48, 48);

        remainingLives = 3;
        points = 0;
        coins = 0;
        invincibilityTimer = 0;

        ImageLoader imageLoader = new ImageLoader(type);
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

    public boolean onTouchEnemy(GameEngine engine) {

        if (!heroForm.isSuper() && !heroForm.ifCanShootFire()) {
            remainingLives--;
            if (engine.getHero() != null) {
                engine.getHero().setRemainingLives(engine.getHero().getRemainingLives() - 1);
            }
            engine.playMarioDies();
            return true;
        } else {
            engine.shakeCamera();
            heroForm = heroForm.onTouchEnemy(engine.getImageLoader());
            if (engine.getHero() != null) {
                engine.getHero().setHeroForm(engine.getHero().getHeroForm().onTouchEnemy(engine.getImageLoader()));
            }
            setDimension(48, 48);
            return false;
        }
    }

    public void onTouchHole(GameEngine engine) {
        remainingLives = remainingLives - 1;
        heroForm = heroForm.onTouchEnemy(engine.getImageLoader());
        if (engine.getHero() != null) {
            engine.getHero().setRemainingLives(engine.getHero().getRemainingLives() - 1);
            engine.getHero().setHeroForm(engine.getHero().getHeroForm().onTouchEnemy(engine.getImageLoader()));
        }
        engine.playMarioDies();
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
            setVelY(15);
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
            setVelX(8);
        } else if (camera.getX() < getX()) {
            setVelX(-8);
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

    public void setTypesOwned(List<Integer> typesOwned) {
        this.typesOwned = typesOwned;
    }

    public List<Integer> getTypesOwned() {
        return typesOwned;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }
}
