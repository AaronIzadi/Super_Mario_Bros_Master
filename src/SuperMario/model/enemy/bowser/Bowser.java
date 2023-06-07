package SuperMario.model.enemy.bowser;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.input.ImageLoader;
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
    private ArrayList<Fire> fire;
    private Bomb bomb;
    private boolean isGrabAttackOn;
    private boolean hasTouchedGround;

    public Bowser(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(104, 120);
        hitPoints = HitPoints.getInstance();
        setHp(20);
        setVelX(-1.5);
        fire = new ArrayList<>();
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
            int random = (int) (Math.random() * 4);
            if (random == 0 && hp <= 10) {
                bomb();
            } else if (random == 1) {
                fire();
            } else if (random == 2) {
                jumpAttack();
            } else {
                grabAttack(hero);
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

        bomb = new Bomb(x, y, ImageLoader.getInstance().getBomb());

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
        timer.schedule(task, 4000 + 1000);
    }

    public ArrayList<Fire> getFire() {
        return fire;
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

    public void setHasTouchedGround(boolean hasTouchedGround) {
        this.hasTouchedGround = hasTouchedGround;
    }

    public boolean hasTouchedGround() {
        return hasTouchedGround;
    }
}
