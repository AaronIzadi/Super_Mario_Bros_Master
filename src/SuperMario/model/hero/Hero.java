package SuperMario.model.hero;

import SuperMario.graphic.manager.Camera;
import SuperMario.logic.GameEngine;
import SuperMario.model.GameObject;
import SuperMario.input.ImageLoader;
import SuperMario.model.weapon.Axe;
import SuperMario.model.weapon.Fireball;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Hero extends GameObject {
    private int remainingLives;
    private int coins;
    private int points;
    private double invincibilityTimer;
    private HeroForm heroForm;
    private int type;
    private boolean toRight;
    private boolean isSitting;
    private boolean tookStar;
    private boolean isAxeActivated;
    private boolean canActivateAxe;
    private Axe axe;

    public Hero(double x, double y) {
        super(x, y, null);
        setDimension(48, 48);
        manageHero(HeroForm.SMALL, false, false);
    }

    public Hero(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, null);
        this.type = type;
        setDimension(width, height);
        manageHero(heroForm, isSuper, canShootFire);
    }

    private void manageHero(int heroForm, boolean isSuper, boolean canShootFire) {
        remainingLives = 3;
        points = 0;
        coins = 0;
        invincibilityTimer = 0;
        toRight = true;
        tookStar = false;

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.setHeroType(type);
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames(heroForm);
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames(heroForm);

        this.heroForm = new HeroForm(leftFrames, rightFrames, isSuper, canShootFire, type);
        setStyle(this.heroForm.getCurrentStyle(toRight, false, false, false));
    }

    @Override
    public void draw(Graphics g) {
        boolean movingInX = (getVelX() != 0);
        boolean movingInY = (getVelY() != 0);

        setStyle(heroForm.getCurrentStyle(toRight, movingInX, movingInY, isSitting));

        super.draw(g);

        if (axe != null) {
            if (toRight) {
                axe.setX(getX() + 24);
            } else {
                axe.setX(getX() - 48);
            }
            axe.setVelX(getVelX());

            axe.setVelY(getVelY());
            axe.setY(getY());
        }
    }

    public abstract void jump();

    public void sit() {
        if (isSuper() && !isJumping()) {
            isSitting = true;
            getDimension().height = 72;
        }
    }

    public abstract void move(boolean toRight, Camera camera);

    public void setTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setTookStar(false);
                if (!GameEngine.getInstance().isMute()) {
                    GameEngine.getInstance().resumeBackground();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 15000);
    }

    public void setTimerToRun() {
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

        if (!ifTookStar()) {
            if (!heroForm.isSuper() && !heroForm.ifCanShootFire()) {
                heroDies(engine, 20);
                return true;
            } else {
                engine.shakeCamera();
                heroForm.setSuper(false);
                heroForm.setCanShootFire(false);
                heroForm.onTouchEnemy(engine.getImageLoader());
                setDimension(48, 48);
                setY(getY() + getDimension().getHeight());
                return false;
            }
        }
        return false;
    }

    public void onTouchBorder(GameEngine engine) {
        heroDies(engine, 30);
        engine.playHeroFalls();
    }

    public void heroDies(GameEngine engine, int lostScore) {
        remainingLives--;
        points = points > lostScore ? points - lostScore : 0;
        if (remainingLives == 0) {
            engine.playGameOver();
        } else {
            engine.playHeroDies();
        }
        heroForm.onTouchEnemy(engine.getImageLoader());
        setDimension(48, 48);
    }

    public Fireball fire() {
        return heroForm.fire(toRight, getX(), getY());
    }

    public boolean ifCanActivateAxe() {
        canActivateAxe = coins >= 3 && isSuper();
        return canActivateAxe;
    }

    public Axe getAxe() {
        if (toRight) {
            axe = new Axe(getX() + 24, getY(), ImageLoader.getInstance().getAxeUpRight(), this);
        } else {
            axe = new Axe(getX() - 48, getY(), ImageLoader.getInstance().getAxeUpRight(), this);
        }
        return axe;
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
        setY(100);
        setJumping(false);
        setFalling(true);
    }

    @Override
    public BufferedImage getStyle() {
        return super.getStyle();
    }

    public void acquireCoinForToad() {
        setExtraCoin();
    }

    public void setTookStar(boolean tookStar) {
        this.tookStar = tookStar;
    }

    public boolean ifTookStar() {
        return tookStar;
    }

    public boolean isSitting() {
        return isSitting;
    }

    public void setSitting(boolean sitting) {
        isSitting = sitting;
    }

    public boolean isAxeActivated() {
        return isAxeActivated;
    }

    public void setAxeActivated(boolean axeActivated) {
        isAxeActivated = axeActivated;
    }

}