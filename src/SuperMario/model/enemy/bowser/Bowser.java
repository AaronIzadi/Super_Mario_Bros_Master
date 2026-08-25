package SuperMario.model.enemy.bowser;

import SuperMario.graphic.view.animation.Animation;
import SuperMario.logic.enemy.BowserLogic;
import SuperMario.model.enemy.Enemy;
import SuperMario.model.hero.Hero;
import SuperMario.model.map.HitPoints;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bowser extends Enemy {

    private int hp;
    private boolean isCoolDownFinished = true;
    private int cooldownTicks;
    private int hpRecoveryTicks;
    private int grabReactionTicks;
    private int postGrabRecoveryTicks;
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

    public Bowser(double x, double y, BufferedImage style, HitPoints hitPoints) {
        super(x, y, style);
        setDimension(125, 144);
        this.hitPoints = hitPoints;
        BowserLogic.setHp(this, 20);
        setVelX(-1.5);
        fire = new ArrayList<>();
        bomb = new ArrayList<>();
    }

    public int getHp() {
        return hp;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setLeftFrames(BufferedImage[] frames) {
        this.leftFrames = frames;
    }

    public void setRightFrames(BufferedImage[] frames) {
        this.rightFrames = frames;
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
        return isCoolDownFinished && cooldownTicks <= 0;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public int getHpRecoveryTicks() {
        return hpRecoveryTicks;
    }

    public void setHpRecoveryTicks(int hpRecoveryTicks) {
        this.hpRecoveryTicks = hpRecoveryTicks;
    }

    public int getGrabReactionTicks() {
        return grabReactionTicks;
    }

    public void setGrabReactionTicks(int grabReactionTicks) {
        this.grabReactionTicks = grabReactionTicks;
    }

    public int getPostGrabRecoveryTicks() {
        return postGrabRecoveryTicks;
    }

    public void setPostGrabRecoveryTicks(int postGrabRecoveryTicks) {
        this.postGrabRecoveryTicks = postGrabRecoveryTicks;
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
