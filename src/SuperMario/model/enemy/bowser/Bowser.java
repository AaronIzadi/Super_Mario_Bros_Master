package SuperMario.model.enemy.bowser;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
import SuperMario.model.GameObject;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.HitPoints;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private Fire fire;
    private Bomb bomb;
    private boolean isGrabAttackOn;

    public Bowser(double x, double y, BufferedImage style) {
        super(x, y, style);
        hitPoints = HitPoints.getInstance();
        setHp(20);
        setVelX(-1.5);
    }

    @Override
    public void draw(Graphics g) {
        if (getVelY() > 0) {
            setFalling(true);
            setJumping(false);
            BufferedImage style = isToRight() ? ImageLoader.getInstance().getBossUpSideRight() : ImageLoader.getInstance().getBossUpSideLeft();
            setStyle(style);
        } else {
            setJumping(false);
            setFalling(false);
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

    public GameObject attack(Hero hero) {

        if (isCoolDownFinished) {
//            return bomb();
            int random = (int) (Math.random() * 4);
            if (random == 0 && hp <= 10) {
                return bomb();
            } else if (random == 1) {
                return fire();
            } else if (random == 2) {
                jumpAttack();
            } else {
               grabAttack(hero);
            }
        }

        return null;
    }

    private Fire fire() {

        isCoolDownFinished = false;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000 + 2000);

        BufferedImage style = isToRight() ? ImageLoader.getInstance().getFireballRight() : ImageLoader.getInstance().getFireballLeft();

        int random = (int) (Math.random() * 2);
        if (random == 0) {
            fire = new Fire(getX(), getY() + 24, style, isToRight());
        } else {
            fire = new Fire(getX(), getY() + 72, style, isToRight());
        }
        return fire;
    }

    private Bomb bomb() {

        isCoolDownFinished = false;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000 + 2000);

        double x = isToRight() ? getX() + 78 : getX();
        double y = getY() + 68;

        bomb = new Bomb(x, y, ImageLoader.getInstance().getBomb());

        return bomb;
    }


    private void jumpAttack() {

        isCoolDownFinished = false;

        if (!isFalling() && !isJumping()) {
            setVelY(5);
            setJumping(true);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000 + 2000);

    }

    private void grabAttack(Hero hero) {

        isCoolDownFinished = false;
        isGrabAttackOn = true;

        if (hero.getBottomBounds().getY() >= 720 - (3 * 48)) {
            setVelX(getVelX() * 3);
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isCoolDownFinished = true;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 4000 + 2000);
    }

    public Fire getFire() {
        return fire;
    }

    public void setFire(Fire fire) {
        this.fire = fire;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    public boolean isGrabAttackOn() {
        return isGrabAttackOn;
    }

    public void setGrabAttackOn(boolean grabAttackOn) {
        isGrabAttackOn = grabAttackOn;
    }
}
