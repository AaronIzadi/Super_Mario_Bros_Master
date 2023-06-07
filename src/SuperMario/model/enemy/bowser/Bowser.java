package SuperMario.model.enemy.bowser;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.logic.GameEngine;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.HitPoints;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Bowser extends Enemy {

    private int hp;
    private boolean isCoolDownFinished = true;
    private final HitPoints hitPoints;
    private BufferedImage[] rightFrames;
    private BufferedImage[] leftFrames;
    private Animation rightAnimation;
    private Animation leftAnimation;
    private final ArrayList<Fire> fire;
    private final ArrayList<Bomb> bomb;
    private boolean isGrabAttackOn = false;
    private boolean hasTouchedGround;
    private boolean canHurt = false;

    public Bowser(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(104, 120);
        hitPoints = HitPoints.getInstance();
        setHp(20);
        setVelX(-1.5);
        fire = new ArrayList<>();
        bomb = new ArrayList<>();
    }

    @Override
    public void draw(Graphics g) {
        if (Math.ceil(getVelY()) < 0) {
            BufferedImage style = isToRight() ? ImageLoader.getInstance().getBossUpSideRight() : ImageLoader.getInstance().getBossUpSideLeft();
            setStyle(style);
        } else {
            animate();
        }
        super.draw(g);
    }

    public void setHp(int hp) {
        this.hp = hp;
        hitPoints.setStyle(hp);
    }

    public int getHp() {
        return hp;
    }

    public void setLeftFrames(BufferedImage[] frames) {
        this.leftFrames = frames;
    }

    public void setRightFrames(BufferedImage[] frames) {
        this.rightFrames = frames;
    }

    public void setFrames() {
        rightAnimation = new Animation(rightFrames);
        leftAnimation = new Animation(leftFrames);
    }

    public void animate() {
        Animation animation = isToRight() ? rightAnimation : leftAnimation;
        boolean isAnimationTicked = animation.animate(10);
        if (isAnimationTicked) {
            setStyle(animation.getCurrentFrame());
        }
    }

    public void attack(Hero hero) {

        if (isCoolDownFinished) {
            int random;

            if (hp > 10) {
                random = (int) (Math.random() * 3);
            } else {
                random = (int) (Math.random() * 4);
            }

            if (random == 0) {
                grabAttack(hero);
            } else if (random == 1) {
                fire();
            } else if (random == 2) {
                jumpAttack();
            } else {
                bomb();
            }
        }
    }

    private void fire() {

        isCoolDownFinished = false;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000 + 1000);

        BufferedImage style = isToRight() ? ImageLoader.getInstance().getFireballRight() : ImageLoader.getInstance().getFireballLeft();
        double x = isToRight() ? getX() + 9 : getX() - 1;

        int random = (int) (Math.random() * 2);
        if (random == 0) {
            fire.add(new Fire(x, getY() + 24, style, isToRight()));
        } else {
            fire.add(new Fire(x, getY() + 72, style, isToRight()));
        }
        GameEngine.getInstance().playBowserFireBall();
    }

    private void bomb() {

        isCoolDownFinished = false;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000 + 1000);

        double x = isToRight() ? getX() + 78 : getX();
        double y = getY() + 68;

        bomb.add(new Bomb(x, y, ImageLoader.getInstance().getBomb()));
        GameEngine.getInstance().playBowserFireBall();
    }


    private void jumpAttack() {

        isCoolDownFinished = false;

        if (!isJumping()) {
            setJumping(true);
            setVelY(7);
            setHasTouchedGround(false);
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000 + 1000);

    }

    private void grabAttack(Hero hero) {

        if (!isGrabAttackOn) {
            isCoolDownFinished = false;
            isGrabAttackOn = true;
            canHurt = true;

            moveFaster();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!hero.isGrabbed()) {
                        moveNormal();
                        isCoolDownFinished = true;
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 4000);
        }
    }

    private void moveFaster() {
        if (isToRight()) {
            setVelX(4.5);
        } else {
            setVelX(-4.5);
        }
    }

    public void moveNormal() {
        if (isToRight()) {
            setVelX(1.5);
        } else {
            setVelX(-1.5);
        }
    }

    public void stopMoving() {
        setVelX(0);
    }

    public ArrayList<Fire> getFire() {
        return fire;
    }

    public ArrayList<Bomb> getBomb() {
        return bomb;
    }

    public boolean isGrabAttackOn() {
        return isGrabAttackOn;
    }

    public void setGrabAttackOn(boolean grabAttackOn) {
        isGrabAttackOn = grabAttackOn;
    }

    public void setHasTouchedGround(boolean hasTouchedGround) {
        this.hasTouchedGround = hasTouchedGround;
    }

    public boolean hasTouchedGround() {
        return hasTouchedGround;
    }

    public void setCoolDownFinished(boolean coolDownFinished) {
        isCoolDownFinished = coolDownFinished;
    }

    public void setCanHurt(boolean canHurt) {
        this.canHurt = canHurt;
    }

    public boolean canHurt() {
        return canHurt;
    }
}
