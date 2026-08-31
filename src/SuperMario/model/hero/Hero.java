package SuperMario.model.hero;

import SuperMario.config.GameConstants;
import SuperMario.logic.hero.HeroLogic;
import SuperMario.model.GameObject;
import SuperMario.model.weapon.Hammer;

import java.awt.image.BufferedImage;

public abstract class Hero extends GameObject {

    private int remainingLives;
    private int coins;
    private int points;
    private double invincibilityTimer;
    private HeroForm heroForm;
    private int type;
    private boolean toRight;
    private boolean crouching;
    private boolean tookStar;
    private boolean isGrabbed;
    private boolean isHammerActivated;
    private Hammer hammer;
    private int numberOfTryToEscape;
    private double standingStart;
    private double standingTimer;
    private int starPowerTicks;
    private int starRunTicks;
    private int hammerCooldownTicks;
    private int grabTimeoutTicks;

    public Hero(double x, double y) {
        super(x, y, null);
        setDimension(GameConstants.HERO_DEFAULT_SIZE, GameConstants.HERO_DEFAULT_SIZE);
        HeroLogic.initializeNewHero(this);
    }

    public Hero(double x, double y, int width, int height, int type, int heroForm, boolean isSuper, boolean canShootFire) {
        super(x, y, null);
        this.type = type;
        setDimension(width, height);
        HeroLogic.initializeHero(this, heroForm, isSuper, canShootFire);
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

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTookStar(boolean tookStar) {
        this.tookStar = tookStar;
    }

    public boolean hasStarPower() {
        return tookStar;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public boolean isHammerActivated() {
        return isHammerActivated;
    }

    public void setHammerActivated(boolean hammerActivated) {
        isHammerActivated = hammerActivated;
    }

    public void setGrabbed(boolean grabbed) {
        isGrabbed = grabbed;
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void addNumberOfTryToEscape() {
        numberOfTryToEscape++;
    }

    public void setNumberOfTryToEscape(int numberOfTryToEscape) {
        this.numberOfTryToEscape = numberOfTryToEscape;
    }

    public int getNumberOfTryToEscape() {
        return numberOfTryToEscape;
    }

    public Hammer getHammer() {
        return hammer;
    }

    public void setHammer(Hammer hammer) {
        this.hammer = hammer;
    }

    public boolean isHammerCoolDownFinished() {
        return hammerCooldownTicks <= 0;
    }

    public double getStandingStart() {
        return standingStart;
    }

    public void setStandingStart(double standingStart) {
        this.standingStart = standingStart;
    }

    public double getStandingTimer() {
        return standingTimer;
    }

    public void setStandingTimer(double standingTimer) {
        this.standingTimer = standingTimer;
    }

    public int getStarPowerTicks() {
        return starPowerTicks;
    }

    public void setStarPowerTicks(int starPowerTicks) {
        this.starPowerTicks = starPowerTicks;
    }

    public int getStarRunTicks() {
        return starRunTicks;
    }

    public void setStarRunTicks(int starRunTicks) {
        this.starRunTicks = starRunTicks;
    }

    public int getHammerCooldownTicks() {
        return hammerCooldownTicks;
    }

    public void setHammerCooldownTicks(int hammerCooldownTicks) {
        this.hammerCooldownTicks = hammerCooldownTicks;
    }

    public int getGrabTimeoutTicks() {
        return grabTimeoutTicks;
    }

    public void setGrabTimeoutTicks(int grabTimeoutTicks) {
        this.grabTimeoutTicks = grabTimeoutTicks;
    }
}
