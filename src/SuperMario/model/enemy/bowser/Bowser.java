package SuperMario.model.enemy.bowser;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.HitPoints;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bowser extends Enemy {

    private int hp;
    private boolean isCoolDownFinished = true;
    private final HitPoints hitPoints;
    private BufferedImage[] rightFrames;
    private BufferedImage[] leftFrames;
    private Animation rightAnimation;
    private Animation leftAnimation;
    private Hero hero;
    private final ArrayList<Fire> fire;
    private final ArrayList<Bomb> bomb;
    private boolean isGrabAttackOn = false;
    private boolean hasTouchedGround;
    private boolean canHurt = false;
    private boolean jump = false;

    public Bowser(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(125, 144);
        hitPoints = HitPoints.getInstance();
        BowserLogic.setHp(this, 20);
        setVelX(-1.5);
        fire = new ArrayList<>();
        bomb = new ArrayList<>();
    }

    @Override
    public void draw(Graphics g) {
        BowserLogic.draw(this, g);
    }

    public void setHp(int hp) {
        BowserLogic.setHp(this, hp);
    }

    public void setHero(Hero hero) {
        this.hero = hero;
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
        BowserLogic.setFrames(this);
    }

    public void animate() {
        BowserLogic.animate(this);
    }

    @Override
    public void updateLocation() {
        BowserLogic.update(this);
    }

    public void attack() {
        BowserLogic.attack(this);
    }

    public void canJump(boolean isFar) {
        BowserLogic.canJump(this, isFar);
    }

    public void jump() {
        BowserLogic.jump(this);
    }

    public void moveNormal(boolean toRight) {
        BowserLogic.moveNormal(this, toRight);
    }

    public void stopMoving() {
        BowserLogic.stopMoving(this);
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

    public int getHpValue() {
        return hp;
    }

    public void setHpValue(int hp) {
        this.hp = hp;
    }

    public HitPoints getHitPoints() {
        return hitPoints;
    }

    public boolean isCoolDownFinished() {
        return isCoolDownFinished;
    }

    public Hero getHero() {
        return hero;
    }

    public Animation getRightAnimation() {
        return rightAnimation;
    }

    public void setRightAnimation(Animation rightAnimation) {
        this.rightAnimation = rightAnimation;
    }

    public Animation getLeftAnimation() {
        return leftAnimation;
    }

    public void setLeftAnimation(Animation leftAnimation) {
        this.leftAnimation = leftAnimation;
    }

    public BufferedImage[] getRightFrames() {
        return rightFrames;
    }

    public BufferedImage[] getLeftFrames() {
        return leftFrames;
    }

    public void setJumpIntent(boolean jump) {
        this.jump = jump;
    }

    public boolean isJumpIntent() {
        return jump;
    }
}
